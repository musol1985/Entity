package com.entity.bean.custom;

import java.lang.reflect.Method;

import com.entity.anot.components.input.Input;
import com.entity.bean.AnnotationMethodBean;

public class InputBean extends AnnotationMethodBean<Input>{
	protected boolean digital;
	
	public InputBean(Method m, Class<Input> annotationClass, boolean digital) throws Exception {
		super(m, annotationClass);

		this.digital=digital;
	}

	public boolean isDigital() {
		return digital;
	}

	
	
}
