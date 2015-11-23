package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.CamNode;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;
import com.jme3.scene.CameraNode;

public class CameraInjector<T extends IEntity> extends ListBeanInjector<AnnotationFieldBean<CamNode>, T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(CamNode.class)){
			beans.add(new AnnotationFieldBean<CamNode>(f, CamNode.class));
		}
	}
	
	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(AnnotationFieldBean<CamNode> bean:beans){
			bean.getField().set(e, new CameraNode(bean.getAnnot().name(), EntityManager.getCamera()));
		}
	}

}
