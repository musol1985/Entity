package com.entity.test;

import java.lang.reflect.Field;

import com.entity.core.EntityManager;

public class TestEnhancer {

	
	
	public static void main(String[] args) throws Exception{
		CGLIBTest2 bm=(CGLIBTest2) EntityManager.instanceGeneric(CGLIBTest2.class);
		System.out.println(bm);
		bm.test3();
		//bm.test2();
		
		/*Field f=bm.getClass().getSuperclass().getDeclaredField("model1");
		f.setAccessible(true);
		BasicModel bm1=(BasicModel)f.get(bm);
		
		for(Class c:bm.getClass().getInterfaces()){
			System.out.println(c.getName());
		}
		
		System.out.println(bm1);*/
	}
	

}
