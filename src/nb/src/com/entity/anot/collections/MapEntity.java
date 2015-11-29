package com.entity.anot.collections;

import com.entity.core.IEntity;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapEntity {
	MapEntryEntity	[] entries();
	boolean attachAllItems() default false;
	String packageItems() default "";
        Class packageItemSubTypeOf() default IEntity.class;
}