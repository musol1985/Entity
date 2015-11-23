package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.entity.anot.components.lights.AmbientLightComponent;
import com.entity.anot.components.lights.DirectionalLightComponent;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.IEntity;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.Filter;

public class LightBean extends AnnotationFieldBean{
	private Filter filter;
	
	
	public LightBean(Field f, Class<? extends Annotation> anot)throws Exception{
		super(f, anot);		
	}

	
	public static boolean isAmbientLight(Field f){
		return f.isAnnotationPresent(AmbientLightComponent.class);
	}
	
	public static boolean isDirectionalLight(Field f){
		return f.isAnnotationPresent(DirectionalLightComponent.class);
	}
	
	public Light createLight(IEntity instance)throws Exception{
		if(annot.annotationType()==AmbientLightComponent.class){
			return createAmbientLight(f, instance);
		}else if(annot.annotationType()==DirectionalLightComponent.class){
			return createDirectionalLight(f, instance);
		}
		throw new Exception("No type defined for light "+annot.getClass());
	}
	
	private Light createAmbientLight(Field f, IEntity e)throws Exception{
		AmbientLightComponent anot=f.getAnnotation(AmbientLightComponent.class);
		Light l=new AmbientLight();
		l.setColor(new ColorRGBA(anot.color()[0],anot.color()[1],anot.color()[2],anot.color()[3]));
		l.getColor().mult(anot.mult());
		f.set(e, l);
		e.getNode().addLight(l);
		
		return l;
	}
	
	private Light createDirectionalLight(Field f, IEntity e)throws Exception{
		DirectionalLightComponent anot=f.getAnnotation(DirectionalLightComponent.class);
		Light l=new DirectionalLight();
		l.setColor(new ColorRGBA(anot.color()[0],anot.color()[1],anot.color()[2],anot.color()[3]));
		l.getColor().mult(anot.mult());
		((DirectionalLight)l).setDirection(new Vector3f(anot.direction()[0],anot.direction()[1],anot.direction()[2]));
		f.set(e, l);
		e.getNode().addLight(l);
		return l;
	}
}
