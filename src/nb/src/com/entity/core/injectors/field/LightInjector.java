package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.components.lights.AmbientLightComponent;
import com.entity.anot.components.lights.DirectionalLightComponent;
import com.entity.bean.custom.LightBean;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;

public class LightInjector<T  extends IEntity>  extends ListBeanInjector<LightBean, T>{

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

}
