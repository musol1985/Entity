package com.entity.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.entity.adapters.listeners.IScrollCameraListener;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScrollCameraNode {
	boolean attach() default true;
        int offset() default 30;
        int speed() default 20;
        int rotateSpeed() default 20;
        /**
         * Method name to callback on update
         * @return
         */
        Class<? extends IScrollCameraListener> listener() default IScrollCameraListener.class;
}
