package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.Sky;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.entity.core.InjectorAttachable;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class SkyInjector<T  extends IEntity> extends BaseInjector<T> implements InjectorAttachable<T>{
	private Field sky;


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		Sky anot=sky.getAnnotation(Sky.class);
		sky.set(e, SkyFactory.createSky(EntityManager.getAssetManager(), anot.texture(), anot.sphereMapping()));
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(Sky.class)){
			f.setAccessible(true);
			sky=f;
		}
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return sky!=null;
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance) throws Exception{
		app.getRootNode().attachChild((Spatial) sky.get(instance));
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance) throws Exception{
		app.getRootNode().detachChild((Spatial) sky.get(instance));
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
