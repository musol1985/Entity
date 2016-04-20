package com.entity.anot.components.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubModelMapComponent {
	String nameStartsWith();	
	boolean rayPickResponse() default false;
}
