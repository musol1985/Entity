package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.effects.WaterEffect;
import com.entity.bean.EffectBean;
import com.entity.core.EntityGame;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;

public class EffectInjector<T  extends IEntity> extends BaseInjector<T> implements InjectorAttachable<T>{
	private List<EffectBean> effects=new ArrayList<EffectBean>();


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(EffectBean effect:effects){
			effect.instance(e);
		}
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
            System.out.println("******************************"+c+"     "+f+"          "+f.isAnnotationPresent(WaterEffect.class));
		if(EffectBean.isWaterEffect(f)){
			f.setAccessible(true);
			effects.add(new EffectBean(f, f.getAnnotation(WaterEffect.class)));
		}
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return effects.size()>0;
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance) throws Exception{
		for(EffectBean effect:effects){
			app.addPostProcessor(effect.getFilter());
		}		
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance) throws Exception{
		for(EffectBean effect:effects){
			app.removePostProcessor(effect.getFilter());
		}	
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
