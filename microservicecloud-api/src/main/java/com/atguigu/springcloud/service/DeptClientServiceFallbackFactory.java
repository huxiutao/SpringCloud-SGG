package com.atguigu.springcloud.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.atguigu.springcloud.entities.Dept;

import feign.hystrix.FallbackFactory;

/**
 * 服务熔断：一般是某个服务故障或者异常引起的，类似现实世界中的“保险丝”，当某个异常条件被触发时，直接熔断整个服务，而不是一直等着此服务超时。
 * 
 * 服务降级：
 * 	所谓降级，一般是从整体负荷考虑。就是当某个服务熔断之后，服务器将不再被调用。
 * 	此时，客户端可以自己准备一个本地的fallback回调，返回一个缺省值。
 * 这样做，虽然服务水平下降了，但依然可用。
 * @author Xuekao.Hu
 *
 */
@Component//不要忘记添加，不要忘记添加
public class DeptClientServiceFallbackFactory implements FallbackFactory<DeptClientService> {

	@Override
	public DeptClientService create(Throwable arg0) {
		// TODO Auto-generated method stub
		return new DeptClientService() {

			@Override
			public Dept get(long id) {
				// TODO Auto-generated method stub
				return new Dept().setDeptno(id)
			               .setDname("该ID："+id+"没有没有对应的信息,Consumer客户端提供的降级信息,此刻服务Provider已经关闭")
			               .setDb_source("no this database in MySQL");
			}

			@Override
			public List<Dept> list() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean add(Dept dept) {
				// TODO Auto-generated method stub
				return false;
			}
			
		};
	}

}
