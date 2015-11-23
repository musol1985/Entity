package com.entity.bean;

import java.lang.reflect.Field;

import com.entity.core.IEntity;

public class SingletonBean {
	private Object instance;
	private Field f;
	private boolean singleton;
	
	public SingletonBean(Object instance, Field f) {
		this.instance = instance;
		this.f = f;
		this.singleton=true;
	}
	public SingletonBean(Field f){
		this.f=f;
		this.singleton=false;
	}
	public Object getInstance()throws Exception{
		if(singleton)
			return instance;
		return f.getType().newInstance();
	}	
	public Object getInstance(IEntity entity)throws Exception{
		if(singleton)
			return instance;
		return f.get(entity);
	}
	public Field getF() {
		return f;
	}
	public boolean isSingleton(){
		return singleton;
	}
	
}
