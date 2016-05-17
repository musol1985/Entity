package com.entity.modules.gui.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ButtonGUI {
    String icon() default "";
    String imgBack() default "";
    String imgHover() default "";
    String imgDisabled() default "";
    boolean enabled() default true;
    SpriteGUI sprite();
}
