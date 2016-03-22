package com.entity.test;

import com.entity.anot.OnExecutor;
import com.entity.core.items.BaseService;

public class CGLIBTest extends BaseService {
	public void test(){
		System.out.println("test"+Thread.currentThread());
		test2(new Object());
	}
	
	@OnExecutor
	public void test2(Object obj){
		System.out.println("test2"+Thread.currentThread());
	}
}
