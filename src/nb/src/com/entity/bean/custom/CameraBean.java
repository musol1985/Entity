package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.entity.adapters.listeners.IFollowCameraListener;
import com.entity.anot.FollowCameraNode;
import com.entity.anot.ScrollCameraNode;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;

public class CameraBean extends AnnotationFieldBean{
	private Annotation camera;
	private Field listener;
	
	public CameraBean(Class c, Field f, Class<? extends Annotation> anot)throws Exception{
		super(f, anot);			
		if(isFollowCamera(f)){
			if(!((FollowCameraNode)annot).listenerField().isEmpty()){
				listener=c.getField(((FollowCameraNode)annot).listenerField());
			}
		}
	}

	
	public static boolean isScrollCamera(Field f){
		return EntityManager.isAnnotationPresent(ScrollCameraNode.class,f);
	}
	
	public static boolean isFollowCamera(Field f){
		return EntityManager.isAnnotationPresent(FollowCameraNode.class,f);
	}
	public boolean isScrollCamera(){
		return EntityManager.isAnnotationPresent(ScrollCameraNode.class,f);
	}
	public boolean isFollowCamera(){
		return EntityManager.isAnnotationPresent(FollowCameraNode.class,f);
	}
	
	public boolean hasListener(){
		return listener!=null;
	}
	
	public IFollowCameraListener getListener(Object obj)throws Exception{
		return (IFollowCameraListener)listener.get(obj);
	}
}
