package com.entity.core.injectors.field;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.CamNode;
import com.entity.anot.collections.ListEntity;
import com.entity.anot.collections.ListItemEntity;
import com.entity.bean.AnnotationFieldBean;
import com.entity.bean.custom.CollectionBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;

public class ListInjector<T extends IEntity> extends ListBeanInjector<CollectionBean<ListEntity>, T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(ListEntity.class,f)){
			beans.add(new CollectionBean<ListEntity>(f, ListEntity.class));
		}
	}
	
	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for( CollectionBean<ListEntity> bean:beans){
			List<IEntity> lst=new ArrayList<IEntity>();
			for(ListItemEntity entityAnot:bean.getAnnot().items()){
				IEntity entity=addItem(entityAnot.attach(), bean, e, builder);
				lst.add(entity);
			}
			if(bean.hasPackages()){
				for(Class c:bean.getPackages()){
					IEntity entity=addItem(false, bean, c, e, builder);
					lst.add(entity);
				}			
			}
			//Set the list to the field
			bean.getField().set(e, lst);
		}
	}
	
	private IEntity addItem(boolean attach, CollectionBean<ListEntity> bean, final T e, IBuilder builder) throws Exception{
		return addItem(attach, bean, bean.getField().getType(), e, builder);
	}
	
	private IEntity addItem(boolean attach, CollectionBean<ListEntity> bean, Class classEntity, final T e, IBuilder builder) throws Exception{
		IEntity entity=(IEntity) EntityManager.instanceGeneric(classEntity);			
        entity.onInstance(builder);                     
		
		if(bean.getAnnot().attachAllItems()){
			entity.attachToParent((IEntity) e);
		}else if(attach){
			entity.attachToParent((IEntity) e);
		}
		
		return entity;
	}

}
