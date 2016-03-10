package com.entity.anot.effects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SSAOEffect {    
    float sampleRadius() default 12.94f;
    float intensity() default 43.92f;
    float scale() default 0.33f;
    float bias() default 0.61f;
}
