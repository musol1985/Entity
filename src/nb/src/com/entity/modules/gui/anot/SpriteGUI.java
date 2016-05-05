package com.entity.modules.gui.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SpriteGUI {
	boolean attach() default true;
    String name();
    String texture() default "";
    float[] position() default {};
    String onLeftClick() default "";
    String onRightClick() default "";
}
