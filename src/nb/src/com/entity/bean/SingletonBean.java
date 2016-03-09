package com.entity.bean;

import java.lang.reflect.Field;

import com.entity.core.EntityManager;
import com.entity.core.IEntity;

public class SingletonBean {
	private Object instance;
	private Field f;
	private boolean singleton;
	
	public SingletonBean(Object instance, Field f, boolean singleton) {
		this.instance = instance;
		this.f = f;
		this.singleton=singleton;
	}

	
	public Field getF() {
		return f;
	}
	public boolean isSingleton(){
		return singleton;
	}
	
	public void setInstance(IEntity entity, Object[] args)throws Exception{
		if(singleton){
			if(instance==null){
				instance=EntityManager.instanceGeneric(f.getType(), args);
			}
			f.set(entity, instance);
		}else{
			f.set(entity, EntityManager.instanceGeneric(f.getType(), args));
		}
	}
}
