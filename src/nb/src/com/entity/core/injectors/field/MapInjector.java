package com.entity.core.injectors.field;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.entity.anot.CamNode;
import com.entity.anot.collections.ListEntity;
import com.entity.anot.collections.MapEntity;
import com.entity.anot.collections.MapEntryEntity;
import com.entity.bean.AnnotationFieldBean;
import com.entity.bean.custom.CollectionBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;

public class MapInjector<T extends IEntity> extends ListBeanInjector<CollectionBean<MapEntity>, T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(MapEntity.class,f)){
			beans.add(new CollectionBean<MapEntity>(f, MapEntity.class));
		}
	}
	
	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(CollectionBean<MapEntity> bean:beans){
			Map<String, IEntity> map=new HashMap<String, IEntity>();
			for(MapEntryEntity entry:bean.getAnnot().entries()){
				IEntity entity=addItem(entry.attach(), bean, e, builder);
	            map.put(entry.key(), entity);
			}
			if(bean.hasPackages()){
				for(Class c:bean.getPackages()){
					IEntity entity=addItem(false, bean, c, e, builder);
					map.put(c.getName(), entity);
				}
			}
			bean.getField().set(e, map);
		}
	}

	private IEntity addItem(boolean attach, CollectionBean<MapEntity> bean, final T e, IBuilder builder) throws Exception{
		return addItem(attach, bean, bean.getField().getType(), e, builder);
	}
	
	private IEntity addItem(boolean attach, CollectionBean<MapEntity> bean, Class classEntity, final T e, IBuilder builder) throws Exception{
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
