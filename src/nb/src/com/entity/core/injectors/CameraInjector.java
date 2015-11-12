package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.CamNode;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.jme3.scene.CameraNode;

public class CameraInjector<T extends IEntity> extends BaseInjector<T>{
	private Field camNode;


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		CamNode anot=camNode.getAnnotation(CamNode.class);
		camNode.set(e, new CameraNode(anot.name(), EntityManager.getCamera()));
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(CamNode.class)){
			f.setAccessible(true);
			camNode=f;
		}
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return camNode!=null;
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}



}
