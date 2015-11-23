package com.entity.anot.components.shadows;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jme3.renderer.queue.RenderQueue.ShadowMode;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Shadow {
	public ShadowMode mode() default ShadowMode.Cast;
}
