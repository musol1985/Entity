package com.entity.anot.components.input;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MouseMoveInputMapping {
	String action();
	int[] axis();
	boolean negate() default false;
}
