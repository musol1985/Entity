package com.entity.anot.components.model.collision;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CompSphereCollisionShape {
	float radius() default 1;
}
