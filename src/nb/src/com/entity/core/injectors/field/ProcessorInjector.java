package com.entity.core.injectors.field;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.concurrent.Callable;

import com.entity.anot.effects.BloomEffect;
import com.entity.anot.effects.WaterEffect;
import com.entity.anot.processors.WaterProcessor;
import com.entity.bean.custom.EffectBean;
import com.entity.bean.custom.ProcessorBean;
import com.entity.core.EntityGame;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.BaseInjector;
import com.entity.core.injectors.ListBeanInjector;
import com.jme3.post.SceneProcessor;

public class ProcessorInjector<T  extends IEntity> extends ListBeanInjector<ProcessorBean, T> implements InjectorAttachable<T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		f.setAccessible(true);
		if(ProcessorBean.isWaterProcessor(f))
			beans.add(new ProcessorBean(f, c, WaterProcessor.class));
	}

	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		//Collections.sort(beans);
		for(ProcessorBean effect:beans){
			effect.instance(e);
		}
	}


	@Override
	public <G extends EntityGame> void onAttach(final G app, T instance) throws Exception{				
		for(ProcessorBean effect:beans){
			app.addProcessor((SceneProcessor) effect.getValueField(instance));
		}	
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance) throws Exception{
		for(ProcessorBean effect:beans){
			app.removeProcessor((SceneProcessor)effect.getValueField(instance));
		}	
	}
	@Override
	public int compareTo(BaseInjector t) {
		// TODO Auto-generated method stub
		return 99;
	}
}
