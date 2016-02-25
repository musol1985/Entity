package com.entity.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.entity.core.EntityManager;

public class AnnotationFieldBean<T extends Annotation> {
	protected Field f;
	protected T annot;
	
	public AnnotationFieldBean(Field f, Class<T> annotationClass)throws Exception{
		f.setAccessible(true);
		this.f=f;
		this.annot=EntityManager.getAnnotation(annotationClass,f);
	}

	public Field getField() {
		return f;
	}

	public void setF(Field f) {
		this.f = f;
	}

	public T getAnnot() {
		return annot;
	}

	public void setAnnot(T annot) {
		this.annot = annot;
	}
	
	public Object instance()throws Exception{
		return f.getType().newInstance();
	}
	
	public Object getValueField(Object instance)throws Exception{
		return f.get(instance);
	}
}
