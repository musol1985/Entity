package com.entity.core.injectors;

import java.lang.reflect.Field;

import com.entity.anot.Inject;
import com.entity.anot.Service;
import com.entity.anot.network.WorldService;
import com.entity.bean.SingletonBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.network.core.service.NetWorldService;

public class ListBeanSingletonInjector<T  extends IEntity> extends ListBeanInjector<SingletonBean,T>{
	private Field netWorldServiceField;
	
	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(Inject.class, f)){
			f.setAccessible(true);		
			beans.add(new SingletonBean(f.getType().newInstance(), f, EntityManager.getAnnotation(Inject.class,f).singleton()));							
		}if(EntityManager.isAnnotationPresent(Service.class, f)){
			f.setAccessible(true);
			beans.add(new SingletonBean(f.getType().newInstance(), f, EntityManager.getAnnotation(Service.class,f).singleton()));
		}else if(EntityManager.isAnnotationPresent(WorldService.class, f)){
			f.setAccessible(true);
            netWorldServiceField=f;
			NetWorldService service=EntityManager.getGame().getNet().getWorldService();
			if(service==null){
				service=(NetWorldService) f.getType().newInstance();
				EntityManager.getGame().getNet().setWorldService(service);
			}
		}
	}
	
	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(SingletonBean bean:beans){	
			bean.setInstance(e, params);
		}
		
		if(netWorldServiceField!=null){
			netWorldServiceField.set(e, EntityManager.getGame().getNet().getWorldService());
		}
	}
	
	@Override
	public boolean hasInjections() {
		return beans.size()>0 || netWorldServiceField!=null;
	}

}
