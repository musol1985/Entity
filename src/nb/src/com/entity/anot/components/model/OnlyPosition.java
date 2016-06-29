package com.entity.anot.components.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyPosition {
	public enum TYPE_POSITION{LOCAL, WORLD}

	TYPE_POSITION positionVector() default TYPE_POSITION.LOCAL;
}
