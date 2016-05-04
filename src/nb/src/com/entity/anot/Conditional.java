package com.entity.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Conditional {
	String method();
	boolean includeFieldName() default false;
	boolean includeParams() default false;
}
