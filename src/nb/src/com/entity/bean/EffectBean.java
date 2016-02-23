package com.entity.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.entity.anot.effects.WaterEffect;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.post.Filter;
import com.jme3.water.WaterFilter;

public class EffectBean {
	private Field field;
	private Annotation anot;
	private Filter filter;
	
	
	public EffectBean(Field f, Annotation anot){
		this.field=f;
		this.anot=anot;
	}
	
	public static boolean isEffect(Field f){
		return isWaterEffect(f);
	}
	
	public static boolean isWaterEffect(Field f){
		return EntityManager.isAnnotationPresent(WaterEffect.class,f);
	}
	
	public void instance(IEntity e)throws Exception{
		if(anot instanceof WaterEffect){
			filter=createWater((WaterEffect) anot);
		}
		
		field.set(e, filter);
	}

	public Field getField() {
		return field;
	}

	public Annotation getAnot() {
		return anot;
	}

	public Filter getFilter() {
		return filter;
	}
	
	private Filter createWater(WaterEffect anot){
		WaterFilter filter= new WaterFilter();
		
		filter.setWaterHeight(anot.waterHeight());
		
		return filter;
	}
}
