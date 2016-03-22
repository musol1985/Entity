package com.entity.core.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.entity.anot.Instance;
import com.entity.anot.OnCollision;
import com.entity.anot.OnExecutor;
import com.entity.anot.RayPick;
import com.entity.anot.RunOnGLThread;
import com.entity.core.EntityManager;
import com.entity.core.injectors.ListBeanSingletonInjector;
import com.entity.core.injectors.TriggerInjector;
import com.entity.core.injectors.field.EntityInjector;
import com.entity.core.injectors.field.ListInjector;
import com.entity.core.injectors.field.MapInjector;
import com.entity.core.injectors.input.InputInjector;
import com.entity.core.items.BaseService;
import com.entity.core.items.Model;

public class ServiceBuilder extends Builder<BaseService>{
	private boolean mustEnhance;
	
	private HashMap<Class<BaseService>, Method> collisions=new HashMap<Class<BaseService>, Method>();

	@Override
	public void loadInjectors(Class<BaseService> c) throws Exception {
		addInjector(new InputInjector<BaseService>());
		addInjector(new EntityInjector<BaseService>());
        addInjector(new TriggerInjector<BaseService>());
        addInjector(new ListInjector<BaseService>());
        addInjector(new ListBeanSingletonInjector<BaseService>());
        addInjector(new MapInjector<BaseService>());
	}
	
	
	@Override
	public void loadField(Class<BaseService> cls, Field f) throws Exception {		
		
	}

	@Override
	public void loadMethod(Class<BaseService> c, Method m) throws Exception {
		if(!mustEnhance){
			if(EntityManager.isAnnotationPresent(RunOnGLThread.class,m)){
				mustEnhance=true;
                        }else if(EntityManager.isAnnotationPresent(Instance.class,m)){
                            mustEnhance=true;
                        }else if(EntityManager.isAnnotationPresent(RayPick.class,m)){
                            mustEnhance=true;
                        }else if(EntityManager.isAnnotationPresent(OnExecutor.class,m)){
                            mustEnhance=true;
                        }
		}
		if(EntityManager.isAnnotationPresent(OnCollision.class,m)){
			collisions.put((Class<BaseService>) m.getParameterTypes()[0], m);
		}	
	}

	@Override
	public boolean isMustEnhance() {
		return mustEnhance;
	}
	
	
	public Method collidesWith(Model e){
		return collisions.get(e.getClass());
	}



}
