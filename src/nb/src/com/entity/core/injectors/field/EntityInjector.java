package com.entity.core.injectors.field;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.Entity;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

public class EntityInjector<T  extends IEntity>  extends ListBeanInjector<AnnotationFieldBean<Entity>, T>{
	

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(Entity.class,f)){
			beans.add(new AnnotationFieldBean<Entity>(f, Entity.class));
		}
	}


	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(AnnotationFieldBean<Entity> bean:beans){ 
			IEntity entity=(IEntity) EntityManager.instanceGeneric(bean.getField().getType());
			if(!bean.getAnnot().callOnInject().isEmpty()){
				Method m=entity.getClass().getMethod(bean.getAnnot().callOnInject());
				if(m!=null){
					m.invoke(e, new Object[]{});
				}else{
					log.warning("@Entity.callOnInject method: "+bean.getAnnot().callOnInject()+" doesn't exists in class "+e.getClass().getName());
				}
			}
            entity.onInstance(builder, params);
			bean.getField().set(e, entity);   						
			
			if(!bean.getAnnot().name().isEmpty())
				entity.getNode().setName(bean.getAnnot().name());
			
			if(bean.getAnnot().attach())
				entity.attachToParent((IEntity) e);
		}
	}
}
