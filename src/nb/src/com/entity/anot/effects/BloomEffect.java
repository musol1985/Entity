package com.entity.anot.effects;

import com.jme3.post.filters.BloomFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BloomEffect {
	int downSamplingFactor() default 1;
	float blurScale() default 1.37f;
	float exposurePower() default 4.30f;
	float exposureCutOff() default 0.2f;
	float bloomIntensity() default 1.45f;
        BloomFilter.GlowMode mode() default BloomFilter.GlowMode.Scene;
}
