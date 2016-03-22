package com.entity.test;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.OnExecutor;
import com.entity.core.items.interceptors.EntityMethodInterceptor;
import com.entity.network.core.builders.NetWorldServiceBuilder;

@BuilderDefinition(builderClass=NetWorldServiceBuilder.class, methodInterceptorClass=EntityMethodInterceptor.class)
public class CGLIBTest2 extends CGLIBTest {
	@OnExecutor
	public void test2(Object obj){
		System.out.println("test2"+Thread.currentThread());
	}
	
	
	public void test3(){
		System.out.println("test3");
		test();
	}
}
