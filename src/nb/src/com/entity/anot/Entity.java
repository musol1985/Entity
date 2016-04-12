package com.entity.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Entity {
	boolean attach() default true;
	String name() default "";
	String callOnInject() default "";
	/**
	 * Method name that conditions the injection os this entity
	 * 
	 * @return true(the entity is injected) false(the entity will not inject. Field=null)
	 */
	String conditional() default "";
	
	/**
	 * Substitutes a Node with this name in the model, with the inject of this Entity
	 * It will copy local translation and rotation
	 * @return
	 */
	String substituteNode() default "";
}
