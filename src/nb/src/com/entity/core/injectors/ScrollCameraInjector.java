package com.entity.core.injectors;

import com.entity.adapters.ScrollCameraAdapter;
import com.entity.anot.ScrollCameraNode;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;

public class ScrollCameraInjector<T extends IEntity>  extends BaseInjector<T>{
	private Field f;


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		ScrollCameraNode anot=f.getAnnotation(ScrollCameraNode.class);
                ScrollCameraAdapter entity=(ScrollCameraAdapter) EntityManager.instanceGeneric(f.getType());	
                entity.onInstance(builder);                
		f.set(e, entity);
                
                entity.setSpeed(anot.speed());
                entity.setOffset(anot.offset());
                
                if(anot.attach())
                    entity.attachToParent((IEntity) e);
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(ScrollCameraNode.class)){
			f.setAccessible(true);
			this.f=f;
		}
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return f!=null;
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
