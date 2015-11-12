package com.entity.test;

import java.lang.reflect.Field;

import com.entity.core.EntityManager;

public class TestEnhancer {

	
	
	public static void main(String[] args) throws Exception{
		BasicModel2 bm=EntityManager.instance(BasicModel2.class);
		bm.test();
		
		Field f=bm.getClass().getSuperclass().getDeclaredField("model1");
		f.setAccessible(true);
		BasicModel bm1=(BasicModel)f.get(bm);
		
		for(Class c:bm.getClass().getInterfaces()){
			System.out.println(c.getName());
		}
		
		System.out.println(bm1);
	}
	

}
