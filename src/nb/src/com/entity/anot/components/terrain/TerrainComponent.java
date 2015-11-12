package com.entity.anot.components.terrain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TerrainComponent {
	public int realSize() default 512;
	public int chunkSize() default 64;
	public boolean LOD() default true;
	public String getName() default "";
	public boolean attach() default true;
}
