package com.entity.anot.network;

import com.jme3.network.Message;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Broadcast {
	boolean excludeSender() default true;
	Class<? extends Message>[] filter() default {};
}
