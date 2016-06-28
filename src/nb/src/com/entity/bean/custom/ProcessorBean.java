package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.processors.WaterProcessor;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.post.SceneProcessor;
import com.jme3.water.SimpleWaterProcessor;

public class ProcessorBean extends AnnotationFieldBean implements Comparable<ProcessorBean>{
	private Method onInit;
	
	public ProcessorBean(Field f,Class cls, Class<? extends Annotation> anot)throws Exception{
		super(f, anot);		
		
		if(isWaterProcessor(f)){
			if(!((WaterProcessor)annot).onInit().isEmpty()){
				onInit=cls.getMethod(((WaterProcessor)annot).onInit(), SimpleWaterProcessor.class);
				if(onInit==null)
					throw new Exception("No method onInit: "+((WaterProcessor)annot).onInit());
			}
		}
	}
	
	public static boolean isWaterProcessor(Field f){
		return EntityManager.isAnnotationPresent(WaterProcessor.class, f);
	}
	
	
	public SceneProcessor instance(IEntity e)throws Exception{
		SceneProcessor procesor=null;
		if(annot instanceof WaterProcessor){
			procesor=createWater((WaterProcessor) annot, e);
		}
		if(procesor==null)
			throw new Exception("Processor not found"+annot+" in ProcessorBean.java:instance");
		f.set(e, procesor);
		return procesor;
	}
	
	private SceneProcessor createWater(WaterProcessor anot, IEntity e)throws Exception{
		SimpleWaterProcessor procesor= new SimpleWaterProcessor(EntityManager.getAssetManager());
		if(onInit!=null){
			onInit.invoke(e, procesor);
		}
		/*procesor.setReflectionScene(sceneNode);
		procesor.setDebug(true);*/
		
		return procesor;
	}
	


	@Override
	public int compareTo(ProcessorBean o) {
		return 0;
	}
}
