package com.entity.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.entity.core.EntityManager;

public class AnnotationMethodBean<T extends Annotation> {
	protected Method m;
	protected T annot;
	
	public AnnotationMethodBean(Method m, Class<T> annotationClass)throws Exception{
		m.setAccessible(true);
		this.m=m;
		this.annot=EntityManager.getAnnotation(annotationClass,m);
	}

	public Method getMethod() {
		return m;
	}

	public T getAnnot() {
		return annot;
	}
	
}
