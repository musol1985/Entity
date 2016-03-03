package com.entity.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.entity.core.items.interceptors.EntityMethodInterceptor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) @Inherited
public @interface BuilderDefinition {
	Class builderClass();
	Class methodInterceptorClass() default EntityMethodInterceptor.class;
}
