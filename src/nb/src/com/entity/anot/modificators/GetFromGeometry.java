package com.entity.anot.modificators;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GetFromGeometry {
	String geometry();
}
