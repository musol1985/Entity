package com.entity.core.injectors;

import com.entity.bean.SingletonBean;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;

public abstract class ListBeanSingletonInjector<G extends SingletonBean, T  extends IEntity> extends ListBeanInjector<G,T>{

	

	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(G bean:beans){	
			bean.getF().set(e, bean.getInstance());
		}
	}
}
