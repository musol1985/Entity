package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.entity.anot.effects.WaterEffect;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.post.Filter;
import com.jme3.water.WaterFilter;

public class EffectBean extends AnnotationFieldBean{
	private Filter filter;
	
	
	public EffectBean(Field f, Class<? extends Annotation> anot)throws Exception{
		super(f, anot);		
	}
	
	public static boolean isEffect(Field f){
		return isWaterEffect(f);
	}
	
	public static boolean isWaterEffect(Field f){
		return EntityManager.isAnnotationPresent(WaterEffect.class, f);
	}
	
	public void instance(IEntity e)throws Exception{
		if(annot instanceof WaterEffect){
			filter=createWater((WaterEffect) annot);
		}
		
		f.set(e, filter);
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
