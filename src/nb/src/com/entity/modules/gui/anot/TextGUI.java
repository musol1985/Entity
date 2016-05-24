package com.entity.modules.gui.anot;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.entity.modules.gui.anot.SpriteGUI.ALIGN;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TextGUI {
    boolean attach() default true;
    String name();
    String font();
    float[] position() default {};
    float[] color() default {1f,1f,1f,1f};
    String text() default "";
    ALIGN align() default ALIGN.NONE;
}
