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

public class CollectionBean<T extends Annotation> extends AnnotationFieldBean<T>{
	private List<Class> packages;
	
	
	public CollectionBean(Field f, Class<T> anot)throws Exception{
		super(f, anot);		
		if(anot==ListEntity.class){
			setPackage(((ListEntity)getAnnot()).packageItems());
		}
	}
	
	private void setPackage(String pack)throws Exception{
		if(!pack.isEmpty()){
			Reflections reflections = new Reflections(pack);
			
			 Set<String> messages=reflections.getAllTypes();
			 
			 packages=new ArrayList<Class>();
			 
			 for(String c:messages){
				 packages.add(Class.forName(c));
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
