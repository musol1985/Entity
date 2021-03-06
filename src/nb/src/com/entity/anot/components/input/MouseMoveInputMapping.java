package com.entity.anot.components.input;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MouseMoveInputMapping {
	String action();
	int[] axis();
	boolean negate() default false;
}
