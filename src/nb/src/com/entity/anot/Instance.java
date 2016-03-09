/**
 * 
 */
package com.entity.anot;

import com.entity.core.IEntity;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author martine
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Instance {
	public static final String THIS="this";
	/**
	 * If "" it won't attach, you have to return and attach in the caller
	 * if !="" it will attach to the field with this name 
	 * @return
	 */
    String attachTo() default "";
}
