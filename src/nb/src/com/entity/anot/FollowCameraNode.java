package com.entity.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.entity.adapters.listeners.IFollowCameraListener;
import com.entity.adapters.listeners.IScrollCameraListener;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FollowCameraNode {
    int rotateSpeed() default 20;
    boolean attach() default true;
    boolean debug() default false;
    String followTo() default "";
        /**
         * Method name to callback on update
         * @return
         */
        Class<? extends IFollowCameraListener> listener() default IFollowCameraListener.class;
}
