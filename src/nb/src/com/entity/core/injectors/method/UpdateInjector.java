package com.entity.core.injectors.method;

import java.lang.reflect.Method;

import com.entity.adapters.ControlAdapter;
import com.entity.anot.OnUpdate;
import com.entity.bean.AnnotationMethodBean;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;

public class UpdateInjector<T extends IEntity>  extends ListBeanInjector<AnnotationMethodBean<OnUpdate>,T>{

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {
		if(m.isAnnotationPresent(OnUpdate.class)){
			beans.add(new AnnotationMethodBean<OnUpdate>(m, OnUpdate.class));
		}
	}

	@Override
	public void onInstance(final T item, IBuilder builder) throws Exception {
		for(AnnotationMethodBean<OnUpdate> bean:beans){
			final Method update=bean.getMethod();
			item.getNode().addControl(new ControlAdapter() {
				@Override
				public void update(float tpf) {
					try{
						update.invoke(item, tpf);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});	
		}
	}
}
