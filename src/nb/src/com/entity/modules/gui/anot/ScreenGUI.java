package com.entity.modules.gui.anot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ScreenGUI	 {
	boolean attach() default true;
}
