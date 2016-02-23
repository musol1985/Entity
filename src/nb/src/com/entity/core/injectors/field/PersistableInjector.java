package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.CamNode;
import com.entity.anot.Persistable;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;

public class PersistableInjector<T  extends IEntity>  extends ListBeanInjector<AnnotationFieldBean<Persistable>,T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(Persistable.class,f)){
			beans.add(new AnnotationFieldBean<Persistable>(f, Persistable.class));
		}
	}


	
	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(AnnotationFieldBean<Persistable> bean:beans){
			Object obj=EntityManager.loadPersistable(bean.getAnnot().fileName());
			if(obj==null && bean.getAnnot().newOnNull()){
				obj=bean.getField().getType().newInstance();
			}
			bean.getField().set(e, obj);
		}
	}

}
