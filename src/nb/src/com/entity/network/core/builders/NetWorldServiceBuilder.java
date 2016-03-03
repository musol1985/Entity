package com.entity.network.core.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.OnExecutor;
import com.entity.anot.RunOnGLThread;
import com.entity.core.EntityManager;
import com.entity.core.builders.Builder;
import com.entity.network.core.service.NetWorldService;

public class NetWorldServiceBuilder extends Builder<NetWorldService>{
	private boolean mustEnhance;

	@Override
	public void loadInjectors(Class<NetWorldService> c) throws Exception {

	}
	
	@Override
	public void loadField(Class<NetWorldService> c, Field f) throws Exception {
		
	}

	@Override
	public void loadMethod(Class<NetWorldService> c, Method m) throws Exception {
		if(!mustEnhance){
            if(EntityManager.isAnnotationPresent(OnExecutor.class,m)){
            	mustEnhance=true;
            }else if(EntityManager.isAnnotationPresent(RunOnGLThread.class,m)){
            	mustEnhance=true;
            }
        }
	}


	@Override
	public boolean isMustEnhance() {
		return mustEnhance;
	}
}
