package com.entity.core.injectors.field;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import com.entity.anot.effects.WaterEffect;
import com.entity.bean.custom.EffectBean;
import com.entity.core.EntityGame;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.ListBeanInjector;

public class EffectInjector<T  extends IEntity> extends ListBeanInjector<EffectBean, T> implements InjectorAttachable<T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EffectBean.isWaterEffect(f)){
			f.setAccessible(true);
			beans.add(new EffectBean(f, WaterEffect.class));
		}
	}

	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(EffectBean effect:beans){
			effect.instance(e);
		}
	}


	@Override
	public <G extends EntityGame> void onAttach(final G app, T instance) throws Exception{
		app.enqueue(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				for(EffectBean effect:beans){
					app.addPostProcessor(effect.getFilter());
				}	
				
				return true;
			}
		});
			
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance) throws Exception{
		for(EffectBean effect:beans){
			app.removePostProcessor(effect.getFilter());
		}	
	}

}
