package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.entity.anot.effects.BloomEffect;
import com.entity.anot.effects.SSAOEffect;
import com.entity.anot.effects.WaterEffect;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.post.Filter;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.water.WaterFilter;

public class EffectBean<T extends Filter> extends AnnotationFieldBean implements Comparable<EffectBean>{
	private T filter;
	
	
	public EffectBean(Field f, Class<? extends Annotation> anot)throws Exception{
		super(f, anot);		
	}
	
	public static boolean isEffect(Field f){
		return isWaterEffect(f) || isBloomEffect(f) || isSSAOEffect(f);
	}
	
	public static boolean isWaterEffect(Field f){
		return EntityManager.isAnnotationPresent(WaterEffect.class, f);
	}
	
	public static boolean isBloomEffect(Field f){
		return EntityManager.isAnnotationPresent(BloomEffect.class, f);
	}
	
	public static boolean isSSAOEffect(Field f){
		return EntityManager.isAnnotationPresent(SSAOEffect.class, f);
	}
	
	public void instance(IEntity e)throws Exception{
		if(annot instanceof WaterEffect){
			filter=createWater((WaterEffect) annot);
		}else if(annot instanceof BloomEffect){
			filter=createBloom((BloomEffect) annot);
		}else if(annot instanceof SSAOEffect){
			filter=createSSAO((SSAOEffect) annot);
		}
		
		f.set(e, filter);
	}

	public Filter getFilter() {
		return filter;
	}
	
	private T createWater(WaterEffect anot){
		WaterFilter filter= new WaterFilter();
		
		filter.setWaterHeight(anot.waterHeight());
		
		return (T) filter;
	}
	
	private T createBloom(BloomEffect anot){
		BloomFilter bloom = new BloomFilter();
        bloom.setDownSamplingFactor(anot.downSamplingFactor());
        bloom.setBlurScale(anot.blurScale());
        bloom.setExposurePower(anot.exposurePower());
        bloom.setExposureCutOff(anot.exposureCutOff());
        bloom.setBloomIntensity(anot.bloomIntensity());		
		
		return (T) bloom;
	}
	
	private T createSSAO(SSAOEffect anot){
        return (T) new SSAOFilter(anot.sampleRadius(), anot.intensity(), anot.scale(), anot.bias());
        
	}

	@Override
	public int compareTo(EffectBean o) {
		return 0;
	}
}
