package com.entity.anot.components.lights;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PointLightComponent {
	public float[] color();
	public float radius();
	/**
	 * Name of the node where the light will be attached
	 * if nodePosition=="", it will be attached to the Model
	 * @return
	 */
	public String nodePosition()default "";
	
	/**
	 * If true it add light to the rootnode, else to the model node
	 * @return
	 */
	public boolean rootNode() default true;
}
