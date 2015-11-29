package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

import org.reflections.Reflections;

import com.entity.anot.collections.ListEntity;
import com.entity.anot.collections.MapEntity;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.IEntity;

public class CollectionBean<T extends Annotation> extends AnnotationFieldBean<T>{
	private Set<Class<? extends IEntity>> packages;
	
	
	public CollectionBean(Field f, Class<T> anot)throws Exception{
		super(f, anot);		
		if(anot==ListEntity.class){
			setPackage(((ListEntity)getAnnot()).packageItems(), ((ListEntity)getAnnot()).packageItemSubTypeOf());
		}else if(anot==MapEntity.class){
			setPackage(((MapEntity)getAnnot()).packageItems(), ((MapEntity)getAnnot()).packageItemSubTypeOf());
		}
	}
	
	private void setPackage(String pack, Class<IEntity> cls)throws Exception{
		if(!pack.isEmpty()){
			Reflections reflections = new Reflections(pack);
			
			 packages=reflections.getSubTypesOf(cls);
		}
	}
	
	public boolean hasPackages(){
		return packages!=null;
	}
	
	public Set<Class<? extends IEntity>> getPackages(){
		return packages;
	}
}
