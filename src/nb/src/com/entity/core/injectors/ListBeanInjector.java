package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.core.IEntity;

public abstract class ListBeanInjector<G, T  extends IEntity> extends BaseInjector<T>{
	protected List<G> beans=new ArrayList<G>();

	@Override
	public void onCreate(Class<T> c) throws Exception {
		
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		
	}

	@Override
	public boolean hasInjections() {
		return beans.size()>0;
	}
	
	public List<G> getBeans(){
		return beans;
	}
}
