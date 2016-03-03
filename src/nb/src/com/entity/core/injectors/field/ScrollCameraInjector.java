package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.adapters.ScrollCameraAdapter;
import com.entity.adapters.listeners.IScrollCameraListener;
import com.entity.anot.CamNode;
import com.entity.anot.ScrollCameraNode;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;

public class ScrollCameraInjector<T extends IEntity>  extends ListBeanInjector<AnnotationFieldBean<ScrollCameraNode>, T>{
	
	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(ScrollCameraNode.class,f)){
			beans.add(new AnnotationFieldBean<ScrollCameraNode>(f, ScrollCameraNode.class));
		}
	}

	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(AnnotationFieldBean<ScrollCameraNode> bean:beans){
			ScrollCameraAdapter entity=(ScrollCameraAdapter) EntityManager.instanceGeneric(bean.getField().getType());
			entity.onInstance(builder, null); 

			if(bean.getAnnot().debug())
				entity.debug(true);
			
			bean.getField().set(e, entity);
			
			entity.setValues(bean.getAnnot());
			
			if(bean.getAnnot().attach())
				entity.attachToParent(e);
		}
	}
}
