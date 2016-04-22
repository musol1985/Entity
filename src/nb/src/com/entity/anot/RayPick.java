package com.entity.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RayPick {
	/**
	 * The nodeName in rootNode scene(getChild(String)) 
	 * By default it searchs in root
	 * @return
	 */
	public String NodeName() default "";
	/**
	 * The field name that represents the node to find in 
	 * By default it searchs in root
	 * @return
	 */
	public String Node() default "";
	public Class[] EntityFilter() default {};
}
