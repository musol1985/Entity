package com.entity.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Persistable {
	String fileName() default "";
	boolean newOnNull() default true;
	/**
	 * Method name to callback when the file is created(newOnNull==true)
	 * @return
	 */
	String onNewCallback() default "";
	
	/**
	 * Save the value if it is createt new(newOnNull==true)
	 * @return
	 */
	boolean onNewSave() default false;
}
