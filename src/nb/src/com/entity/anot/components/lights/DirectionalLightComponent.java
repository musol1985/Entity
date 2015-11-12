package com.entity.anot.components.lights;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DirectionalLightComponent {
	public float[] color();
	public float[] direction();
	public float mult() default 1;
}
