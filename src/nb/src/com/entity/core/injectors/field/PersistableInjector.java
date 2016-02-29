package com.entity.core.injectors.field;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(AnnotationFieldBean<Persistable> bean:beans){
			Object obj=EntityManager.loadPersistable(bean.getAnnot().fileName());
			if(obj==null && bean.getAnnot().newOnNull()){
				if(!bean.getAnnot().onNewCallback().isEmpty()){
					Method m=e.getClass().getMethod(bean.getAnnot().onNewCallback());
					if(m!=null){
						obj=m.invoke(e, new Object[]{});
					}else{
						throw new Exception("Method "+bean.getAnnot().onNewCallback()+"doesn't exist in "+e.getClass().getName()+" for the @Persistable.callbackOnNew annotation");
					}
				}else{
					obj=bean.getField().getType().newInstance();
				}
				if(bean.getAnnot().onNewSave()){
					EntityManager.savePersistable(bean.getAnnot().fileName(), obj);
				}
			}
			bean.getField().set(e, obj);
		}
	}

}
