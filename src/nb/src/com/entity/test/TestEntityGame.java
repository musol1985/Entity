package com.entity.test;

import java.lang.reflect.Method;

import com.entity.core.EntityManager;


public class TestEntityGame {

	
	public static void main(String[] args)throws Exception{
		BasicModel3 bm=new BasicModel3();
		Method m=bm.getClass().getMethod("test", new Class[]{Boolean.class});
		System.out.println(m);
		
		BasicModel3 model=(BasicModel3) EntityManager.instanceGeneric(BasicModel3.class, true, false);
		System.out.println(model);
		System.out.println(model.model1);
	}


}
