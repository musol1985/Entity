package com.entity.anot.entities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited 
public @interface SceneEntity {
	String name() default "";
	boolean first() default false;
	boolean preLoad() default false;
	boolean singleton() default true;
}
