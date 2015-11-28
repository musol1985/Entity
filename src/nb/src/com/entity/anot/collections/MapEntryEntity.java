package com.entity.anot.collections;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.entity.core.IEntity;

@Retention(RetentionPolicy.RUNTIME)
public @interface MapEntryEntity {
	String key();
	Class<? extends IEntity> entityClass();
	boolean attach() default false;
}
