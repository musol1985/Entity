package com.entity.anot.entities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModelEntity {
	String name() default "";
	String asset() default "";
	boolean smartReference() default true;
	boolean attach() default true;
}
