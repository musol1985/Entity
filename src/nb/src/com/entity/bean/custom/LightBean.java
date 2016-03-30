package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import com.entity.adapters.DirectionalLightShadow;
import com.entity.anot.components.lights.AmbientLightComponent;
import com.entity.anot.components.lights.DirectionalLightComponent;
import com.entity.anot.components.lights.PointLightComponent;
import com.entity.anot.components.shadows.DirectionalShadowComponent;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;

public class LightBean extends AnnotationFieldBean{
	private Annotation shadow;
	
	public LightBean(Field f, Class<? extends Annotation> anot)throws Exception{
		super(f, anot);		
		if(isDirectionalShadow(f)){
			shadow=EntityManager.getAnnotation(DirectionalShadowComponent.class, f);
		}
	}

	public static boolean isPointLight(Field f){
		return EntityManager.isAnnotationPresent(PointLightComponent.class,f);
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
			return createAmbientLight(f, instance, (AmbientLightComponent)annot);
		}else if(annot.annotationType()==DirectionalLightComponent.class){
			return createDirectionalLight(f, instance, (DirectionalLightComponent) annot);
		}else if(annot.annotationType()==PointLightComponent.class){
			return createPointLight(f, instance, (PointLightComponent) annot);
		}
		throw new Exception("No type defined for light "+annot.getClass());
	}
	
	private Light createAmbientLight(Field f, IEntity e, AmbientLightComponent anot)throws Exception{		
		Light l=new AmbientLight();
		l.setColor(new ColorRGBA(anot.color()[0],anot.color()[1],anot.color()[2],anot.color()[3]));
		l.getColor().mult(anot.mult());
		f.set(e, l);
		e.getNode().addLight(l);
		
		return l;
	}
	
	private Light createPointLight(Field f, IEntity e, PointLightComponent anot)throws Exception{
		PointLight l=new PointLight();
		l.setColor(new ColorRGBA(anot.color()[0],anot.color()[1],anot.color()[2],anot.color()[3]));
		l.setRadius(anot.radius());
		f.set(e, l);
		if(anot.rootNode()){
			EntityManager.getCurrentScene().getNode().addLight(l);
		}else{
			e.getNode().addLight(l);
		}
		
		if(!anot.nodePosition().isEmpty()){
			LightControl lc=new LightControl(l);
			Spatial node=e.getNode().getChild(anot.nodePosition());
			if(node==null)
				throw new Exception("The node "+anot.nodePosition()+" doesn't exist and can't add LightControl to it");
			node.addControl(lc);
		}else{
			l.setPosition(e.getNode().getWorldTranslation());
		}
		
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
	
	public void attachShadow(IEntity e, final EntityGame g)throws Exception{
		final Light l=(Light)getValueField(e);
		if(l!=null && l instanceof DirectionalLightShadow){
			g.enqueue(new Callable<Boolean>() {
	
				@Override
				public Boolean call() throws Exception {
					((DirectionalLightShadow)l).attachShadow(g);
					return true;
				}
			});
		}
	}
	
	public void dettachShadow(IEntity e, EntityGame g)throws Exception{
		Light l=(Light)getValueField(e);
		if(l!=null && l instanceof DirectionalLightShadow){
			((DirectionalLightShadow)l).dettachShadow(g);
		}
	}
}
