package com.entity.anot.components.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WheelComponent {
	float[] direction() default {0, -1, 0};
	float[] axe() default {-1, 0, 0};
	float radius() default 0.5f;
	float restLength() default .3f;
	float[] offset();
	boolean frontWheel() default false;
	/**
	 * Wheel node name
	 * @return
	 */
	String nodeName();
}
