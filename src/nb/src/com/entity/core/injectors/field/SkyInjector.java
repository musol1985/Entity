package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.Sky;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.ListBeanInjector;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class SkyInjector<T  extends IEntity> extends ListBeanInjector<AnnotationFieldBean<Sky>,T> implements InjectorAttachable<T>{
	
	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(Sky.class)){
			beans.add(new AnnotationFieldBean<Sky>(f, Sky.class));
		}
	}

	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(AnnotationFieldBean<Sky> bean:beans){
			bean.getField().set(e, SkyFactory.createSky(EntityManager.getAssetManager(), bean.getAnnot().texture(), bean.getAnnot().sphereMapping()));			
		}	
	}


	@Override
	public <G extends EntityGame> void onAttach(G app, T instance) throws Exception{
		for(AnnotationFieldBean<Sky> bean:beans){
			app.getRootNode().attachChild((Spatial) bean.getField().get(instance));
		}		
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance) throws Exception{
		for(AnnotationFieldBean<Sky> bean:beans){
			app.getRootNode().detachChild((Spatial) bean.getField().get(instance));
		}	
	}
}
