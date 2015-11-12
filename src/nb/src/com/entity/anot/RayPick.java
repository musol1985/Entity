package com.entity.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RayPick {
	public String NodeName() default "";
	public String Node() default "";
	public Class[] EntityFilter() default {};
}
