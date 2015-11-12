package com.entity.anot.network;

import com.jme3.network.Message;

public @interface Broadcast {
	boolean excludeSender() default true;
	Class<? extends Message>[] filter() default {};
}
