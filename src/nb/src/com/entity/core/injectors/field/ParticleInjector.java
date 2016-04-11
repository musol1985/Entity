package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.adapters.EffectParticleAdapter;
import com.entity.anot.effects.EffectParticle;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;

public class ParticleInjector<T extends IEntity> extends ListBeanInjector<AnnotationFieldBean<EffectParticle>, T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(EffectParticle.class,f)){
			beans.add(new AnnotationFieldBean<EffectParticle>(f, EffectParticle.class));
		}
	}
	
	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(AnnotationFieldBean<EffectParticle> bean:beans){
			bean.getField().set(e, new EffectParticleAdapter(bean.getAnnot().asset()));
		}
	}

}
