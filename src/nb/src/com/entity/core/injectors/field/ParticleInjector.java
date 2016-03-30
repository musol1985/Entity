package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.adapters.ParticleCache;
import com.entity.anot.components.model.ParticleComponent;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;

public class ParticleInjector<T extends IEntity> extends ListBeanInjector<AnnotationFieldBean<ParticleComponent>, T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(ParticleComponent.class,f)){
			beans.add(new AnnotationFieldBean<ParticleComponent>(f, ParticleComponent.class));
		}
	}
	
	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(AnnotationFieldBean<ParticleComponent> bean:beans){
			bean.getField().set(e, new ParticleCache(bean.getAnnot().asset()));
		}
	}

}
