package com.entity.anot.network;

public @interface MessageListener {
	boolean singleton() default false;
}
