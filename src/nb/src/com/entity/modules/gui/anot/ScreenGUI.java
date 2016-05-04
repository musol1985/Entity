package com.entity.modules.gui.anot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.entity.core.items.Model;

@Retention(RetentionPolicy.RUNTIME)
public @interface ScreenGUI	 {
	boolean attach() default true;
	boolean captureClick() default true;
	boolean captureMouseMove() default true;
}
