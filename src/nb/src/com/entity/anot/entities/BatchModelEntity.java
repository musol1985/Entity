package com.entity.anot.entities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BatchModelEntity {
	String name();
	boolean smartReference() default true;
	boolean attach() default true;
	boolean autoBatch() default true;
}
