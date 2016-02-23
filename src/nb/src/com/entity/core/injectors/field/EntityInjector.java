package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.CamNode;
import com.entity.anot.Entity;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;

public class EntityInjector<T  extends IEntity>  extends ListBeanInjector<AnnotationFieldBean<Entity>, T>{
	

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(Entity.class,f)){
			beans.add(new AnnotationFieldBean<Entity>(f, Entity.class));
		}
	}


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(AnnotationFieldBean<Entity> bean:beans){
			IEntity entity=(IEntity) EntityManager.instanceGeneric(bean.getField().getType());			
            entity.onInstance(builder);
			bean.getField().set(e, entity);   
			
			if(!bean.getAnnot().name().isEmpty())
				entity.getNode().setName(bean.getAnnot().name());
			
			if(bean.getAnnot().attach())
				entity.attachToParent((IEntity) e);
		}
	}
}
