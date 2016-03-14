package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import com.entity.adapters.DirectionalLightShadow;
import com.entity.anot.FollowCameraNode;
import com.entity.anot.ScrollCameraNode;
import com.entity.anot.components.lights.AmbientLightComponent;
import com.entity.anot.components.lights.DirectionalLightComponent;
import com.entity.anot.components.shadows.DirectionalShadowComponent;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class CameraBean extends AnnotationFieldBean{
	private Annotation camera;
	
	public CameraBean(Field f, Class<? extends Annotation> anot)throws Exception{
		super(f, anot);				
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
}
