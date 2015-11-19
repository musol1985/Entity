package com.entity.anot.components.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PhysicsBodyComponent {
	float mass() default 0f;
	boolean attachWorld() default true;
	PhysicsBodyType type() default PhysicsBodyType.RIGID_BODY;
	
	public enum PhysicsBodyType{
		RIGID_BODY, GHOST_BODY, KINEMATIC_BODY
	}
}
