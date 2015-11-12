package com.entity.anot.components.model.collision;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CompBoxCollisionShape {
	float x() default 0;
	float y() default 0;
	float z() default 0;
}
