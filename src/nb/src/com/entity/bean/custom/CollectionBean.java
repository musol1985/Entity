package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.entity.anot.collections.ListEntity;
import com.entity.anot.collections.MapEntity;
import com.entity.bean.AnnotationFieldBean;
import com.jme3.network.serializing.Serializable;

public class CollectionBean<T extends Annotation> extends AnnotationFieldBean<T>{
	private List<Class> packages;
	
	
	public CollectionBean(Field f, Class<T> anot)throws Exception{
		super(f, anot);		
		if(anot==ListEntity.class){
			setPackage(((ListEntity)getAnnot()).packageItems(), null);
		}else if(anot==MapEntity.class){
                    setPackage(((MapEntity)getAnnot()).packageItems(), ((MapEntity)getAnnot()).packageFilter());
                }
	}
	
	private void setPackage(String pack, Class<? extends Annotation> filter)throws Exception{
		if(!pack.isEmpty()){
			Reflections reflections = new Reflections(pack);
			
			 Set<Class<? extends Object>> messages=reflections.getTypesAnnotatedWith(filter);
			 
			 packages=new ArrayList<Class>();
			 
			 for(Class<? extends Object> c:messages){
				 packages.add(c);
			 }
		}
	}
	
	public boolean hasPackages(){
		return packages!=null;
	}
	
	public List<Class> getPackages(){
		return packages;
	}
}
