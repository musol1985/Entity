package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.entity.adapters.DirectionalLightShadow;
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

public class LightBean extends AnnotationFieldBean{
	private Annotation shadow;
	
	public LightBean(Field f, Class<? extends Annotation> anot)throws Exception{
		super(f, anot);		
		if(isDirectionalShadow(f)){
			shadow=EntityManager.getAnnotation(DirectionalShadowComponent.class, f);
		}
	}

	
	public static boolean isAmbientLight(Field f){
		return EntityManager.isAnnotationPresent(AmbientLightComponent.class,f);
	}
	
	public static boolean isDirectionalLight(Field f){
		return EntityManager.isAnnotationPresent(DirectionalLightComponent.class,f);
	}
	
	public static boolean isDirectionalShadow(Field f){
		return EntityManager.isAnnotationPresent(DirectionalShadowComponent.class,f);
	}
	
	public Light createLight(IEntity instance)throws Exception{
		if(annot.annotationType()==AmbientLightComponent.class){
			return createAmbientLight(f, instance);
		}else if(annot.annotationType()==DirectionalLightComponent.class){
			return createDirectionalLight(f, instance, (DirectionalLightComponent) annot);
		}
		throw new Exception("No type defined for light "+annot.getClass());
	}
	
	private Light createAmbientLight(Field f, IEntity e)throws Exception{
		AmbientLightComponent anot=EntityManager.getAnnotation(AmbientLightComponent.class,f);
		Light l=new AmbientLight();
		l.setColor(new ColorRGBA(anot.color()[0],anot.color()[1],anot.color()[2],anot.color()[3]));
		l.getColor().mult(anot.mult());
		f.set(e, l);
		e.getNode().addLight(l);
		
		return l;
	}
	
	private Light createDirectionalLight(Field f, IEntity e, DirectionalLightComponent anot)throws Exception{
		Light l=null;
		
		if(shadow!=null){
			DirectionalShadowComponent shadow=(DirectionalShadowComponent)this.shadow;
			l=new DirectionalLightShadow(shadow.mapSize(), shadow.splits(), shadow.intensity());
		}else{
			l=new DirectionalLight();
		}				
		l.setColor(new ColorRGBA(anot.color()[0],anot.color()[1],anot.color()[2],anot.color()[3]));
		l.getColor().mult(anot.mult());
		((DirectionalLight)l).setDirection(new Vector3f(anot.direction()[0],anot.direction()[1],anot.direction()[2]));
		f.set(e, l);
		e.getNode().addLight(l);
		return l;
	}
	
	public void attachShadow(IEntity e, EntityGame g)throws Exception{
		Light l=(Light)getValueField(e);
		if(l!=null && l instanceof DirectionalLightShadow){
			((DirectionalLightShadow)l).attachShadow(g);
		}
	}
	
	public void dettachShadow(IEntity e, EntityGame g)throws Exception{
		Light l=(Light)getValueField(e);
		if(l!=null && l instanceof DirectionalLightShadow){
			((DirectionalLightShadow)l).dettachShadow(g);
		}
	}
}
