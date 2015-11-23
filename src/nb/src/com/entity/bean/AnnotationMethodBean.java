package com.entity.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationMethodBean<T extends Annotation> {
	protected Method m;
	protected T annot;
	
	public AnnotationMethodBean(Method m, Class<T> annotationClass)throws Exception{
		m.setAccessible(true);
		this.m=m;
		this.annot=m.getAnnotation(annotationClass);
	}

	public Method getMethod() {
		return m;
	}

	public T getAnnot() {
		return annot;
	}
	
}
