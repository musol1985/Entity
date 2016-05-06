package com.entity.modules.gui.anot;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ScreenEntity	 {
	boolean captureClick()default true;
	boolean captureMove()default true;
}
