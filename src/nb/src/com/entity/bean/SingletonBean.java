package com.entity.bean;

import java.lang.reflect.Field;

public class SingletonBean {
	private Object instance;
	private Field f;
	public SingletonBean(Object instance, Field f) {
		this.instance = instance;
		this.f = f;
	}
	public Object getInstance() {
		return instance;
	}
	public Field getF() {
		return f;
	}
	
	
}
