package com.entity.anot.network;

public @interface MessageListener {
	boolean singleton() default false;
	boolean attach() default true;
	boolean ignoreSyncMessages() default true;
}
