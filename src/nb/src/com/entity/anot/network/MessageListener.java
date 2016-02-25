package com.entity.anot.network;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MessageListener {
	boolean attach() default true;
	boolean ignoreSyncMessages() default true;
}
