package com.entity.anot.collections;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.entity.core.IEntity;

@Retention(RetentionPolicy.RUNTIME)
public @interface MapEntryNameEntity {
	String entityName();
	Class<? extends IEntity> entityClass();
	boolean attach() default false;
}
