package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.network.MessageListener;
import com.entity.bean.SingletonBean;
import com.entity.core.EntityGame;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.ListBeanSingletonInjector;

public class MessageListenerInjector<T extends IEntity>  extends ListBeanSingletonInjector<SingletonBean, IEntity> implements InjectorAttachable<T>{

	@Override
	public void loadField(Class<IEntity> c, Field f) throws Exception {
		if(f.isAnnotationPresent(MessageListener.class)){
			f.setAccessible(true);
			if(f.getAnnotation(MessageListener.class).singleton()){
				beans.add(new SingletonBean(f.getType().newInstance(), f));
			}else{
				beans.add(new SingletonBean(f));
			}
		}
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance) throws Exception {
		for(SingletonBean bean:beans){			
			app.addMessageListener((NetworkMessageListener)bean.getInstance(instance));
		}
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance) throws Exception {		
		for(SingletonBean bean:beans){			
			app.removeMessageListener((NetworkMessageListener)bean.getInstance(instance));
		}
	}
}
