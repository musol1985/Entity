package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.components.lights.AmbientLightComponent;
import com.entity.anot.components.lights.DirectionalLightComponent;
import com.entity.core.EntityGame;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class LightInjector<T  extends IEntity>  extends BaseInjector<T>{
	private List<Field> ambientLights=new ArrayList<Field>();
	private List<Field> directionalLights=new ArrayList<Field>();


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(Field f:ambientLights){
			createAmbientLight(f, e);
		}
		for(Field f:directionalLights){
			createDirectionalLight(f, e);
		}
		
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(AmbientLightComponent.class)){
			f.setAccessible(true);
			ambientLights.add(f);
		}else if(f.isAnnotationPresent(DirectionalLightComponent.class)){
			f.setAccessible(true);
			directionalLights.add(f);
		}
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return ambientLights.size()>0 || directionalLights.size()>0;
	}

	private Light createAmbientLight(Field f, Object e)throws Exception{
		AmbientLightComponent anot=f.getAnnotation(AmbientLightComponent.class);
		Light l=new AmbientLight();
		l.setColor(new ColorRGBA(anot.color()[0],anot.color()[1],anot.color()[2],anot.color()[3]));
		l.getColor().mult(anot.mult());
		f.set(e, l);
		if(e instanceof IEntity)
			((IEntity) e).getNode().addLight(l);
		
		return l;
	}
	
	private Light createDirectionalLight(Field f, Object e)throws Exception{
		DirectionalLightComponent anot=f.getAnnotation(DirectionalLightComponent.class);
		Light l=new DirectionalLight();
		l.setColor(new ColorRGBA(anot.color()[0],anot.color()[1],anot.color()[2],anot.color()[3]));
		l.getColor().mult(anot.mult());
		((DirectionalLight)l).setDirection(new Vector3f(anot.direction()[0],anot.direction()[1],anot.direction()[2]));
		f.set(e, l);
		if(e instanceof IEntity)
			((IEntity) e).getNode().addLight(l);
		return l;
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
