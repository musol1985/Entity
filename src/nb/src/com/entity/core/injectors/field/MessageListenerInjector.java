package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.network.MessageListener;
import com.entity.bean.SingletonBean;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.ListBeanSingletonInjector;

public class MessageListenerInjector<T extends IEntity>  extends ListBeanSingletonInjector<SingletonBean, IEntity> implements InjectorAttachable<T>{

	@Override
	public void loadField(Class<IEntity> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(MessageListener.class, f)){
			f.setAccessible(true);
			if(EntityManager.getAnnotation(MessageListener.class, f).singleton()){
				beans.add(new SingletonBean(f.getType().newInstance(), f));
			}else{
				beans.add(new SingletonBean(f));
			}
		}
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance) throws Exception {
		for(SingletonBean bean:beans){	
			NetworkMessageListener listener=(NetworkMessageListener)bean.getInstance(instance);
			listener.setEntity(instance);
			app.getNet().addMsgListener(listener);
		}
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance) throws Exception {		
		for(SingletonBean bean:beans){			
			NetworkMessageListener listener=(NetworkMessageListener)bean.getInstance(instance);
			app.getNet().removeMsgListener(listener);
		}
	}
}
