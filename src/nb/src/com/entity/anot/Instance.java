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
    String attachTo() default "this";
}
