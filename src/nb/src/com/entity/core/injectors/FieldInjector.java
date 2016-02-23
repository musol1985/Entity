package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.Inject;
import com.entity.bean.SingletonBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;

public class FieldInjector<T extends IEntity>  extends BaseInjector<T>{
	private List<Field> fields=new ArrayList<Field>();
	private List<SingletonBean> singletons=new ArrayList<SingletonBean>();


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(Field f:fields){				
			f.set(e, f.getType().newInstance());
		}
		for(SingletonBean singleton:singletons){
			singleton.getF().set(e, singleton.getInstance());
		}
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(Inject.class, f)){
			f.setAccessible(true);
			if(EntityManager.getAnnotation(Inject.class,f).singleton()){
				singletons.add(new SingletonBean(f.getType().newInstance(), f));
			}else{
				fields.add(f);
			}
		}
	}
	


	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return singletons.size()>0 || fields.size()>0;
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
