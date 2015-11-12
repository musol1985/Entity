package com.entity.anot.components.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubModelComponent {
	String name();	
	boolean rayPickResponse() default false;
	boolean attach() default true;
}
