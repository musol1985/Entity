package com.entity.core.injectors.field;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import com.entity.anot.components.lights.AmbientLightComponent;
import com.entity.anot.components.lights.DirectionalLightComponent;
import com.entity.bean.custom.EffectBean;
import com.entity.bean.custom.LightBean;
import com.entity.bean.custom.RigidBodyBean;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.BaseInjector;
import com.entity.core.injectors.ListBeanInjector;
import com.jme3.bullet.control.PhysicsControl;

public class LightInjector<T  extends IEntity>  extends ListBeanInjector<LightBean, T> implements InjectorAttachable<T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(LightBean.isAmbientLight(f)){
			beans.add(new LightBean(f,AmbientLightComponent.class));
		}else if(LightBean.isDirectionalLight(f)){
			beans.add(new LightBean(f,DirectionalLightComponent.class));
		}
	}


	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(LightBean bean:beans){
			bean.createLight(e);
		}
	}

	@Override
	public <G extends EntityGame> void onAttach(final G app, final T instance)throws Exception {
		
		app.enqueue(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				for(LightBean bean:beans){
					bean.attachShadow(instance, app);
				}
				
				return true;
			}
		});
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance)throws Exception {
		for(LightBean bean:beans){
			bean.dettachShadow(instance, app);
		}
	}


	@Override
	public int compareTo(BaseInjector t) {
		// TODO Auto-generated method stub
		return 100;
	}
	
	
}
