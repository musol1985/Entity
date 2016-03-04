package com.entity.anot.components.shadows;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DirectionalShadowComponent {
	public float intensity() default 0.3f;
	public int splits() default 2;
	public int mapSize() default 2048;
	
}
