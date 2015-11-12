package com.entity.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface Injector<T extends IEntity> {
	public void loadField(Class<T> c, Field f)throws Exception;
	public void loadMethod(Class<T> c, Method m)throws Exception;
	public void onCreate(Class<T> c)throws Exception;
	public boolean hasInjections();
	public void onInstance(T item, IBuilder builder)throws Exception;
	

}
