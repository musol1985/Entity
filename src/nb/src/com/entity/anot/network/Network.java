package com.entity.anot.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.entity.network.core.service.NetWorldService;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Network {
	String[] messagesPackage();
	int port() default 4260;
	String gameName();
	int version();
}
