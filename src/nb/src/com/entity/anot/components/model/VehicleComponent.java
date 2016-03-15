package com.entity.anot.components.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VehicleComponent {
	float mass();
	boolean attachWorld() default true;
	float stiffness() default 60.0f;
	float compValue() default .3f;
	float dampValue() default .4f;
	float maxSuspensionForce() default 10000.0f;
	WheelComponent[] wheels();
}
