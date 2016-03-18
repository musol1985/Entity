package com.entity.anot.components.model.collision;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CustomCollisionShape {
	String methodName();
	boolean singleton() default true;
}
