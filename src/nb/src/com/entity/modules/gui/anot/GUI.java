package com.entity.modules.gui.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GUI {
	int[] canvas() default  {1280,720};
	boolean interceptClick() default false;
}
