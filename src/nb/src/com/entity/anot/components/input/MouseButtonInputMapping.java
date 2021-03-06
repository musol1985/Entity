package com.entity.anot.components.input;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MouseButtonInputMapping {
	String action();
	int[] buttons();
	boolean analog() default false;
	boolean digital() default true;
}
