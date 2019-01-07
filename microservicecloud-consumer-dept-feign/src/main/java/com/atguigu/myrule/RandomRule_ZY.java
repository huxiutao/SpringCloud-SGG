package com.atguigu.myrule;

import java.util.List;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

public class RandomRule_ZY extends AbstractLoadBalancerRule {
	
	// 总共被调用的次数，目前要求每台被调用5次
	private int total = 0;
	// 当前提供服务的机器号
	private int currentIndex = 0;
	
    /**
     * Randomly choose from all living servers
     * ILoadBalancer是负载均衡算法
     */
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();	// 活着的服务
            List<Server> allList = lb.getAllServers();	// 所有的服务8001、8002、8003

            System.out.println("----------------------");
            for (Server server2 : upList) {
            	System.out.println(server2.getHostPort());
			}
            System.out.println("----------------------");
            System.out.println();
            System.out.println("++++++++++++++++++++++");
            for (Server server2 : allList) {
            	System.out.println(server2.getHostPort());
			}
            System.out.println("++++++++++++++++++++++");
            
            int serverCount = allList.size();
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes
                 * only get more restrictive.
                 */
                return null;
            }

//            int index = rand.nextInt(serverCount);
//            server = upList.get(index);
            
            if (total<5) {
            	server = upList.get(currentIndex);
            	total++;
            } else {
            	total = 0;
            	currentIndex++;
            	if (currentIndex>=upList.size()) {
            		currentIndex = 0;
            	}
            }
            
            
            

            if (server == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }

        return server;

    }

	@Override
	public Server choose(Object key) {
		return choose(getLoadBalancer(), key);
	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
		// TODO Auto-generated method stub
		
	}
}