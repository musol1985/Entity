package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.network.MessageListener;
import com.entity.anot.network.ServerConnectionsListener;
import com.entity.bean.SingletonBean;
import com.entity.bean.custom.NetworkListenerBean;
import com.entity.core.EntityGame;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.ListBeanInjector;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ConnectionListener;

public class NetworkListenerInjector<T extends IEntity>  extends ListBeanInjector<NetworkListenerBean, IEntity> implements InjectorAttachable<T>{


	
	@Override
	public void loadField(Class<IEntity> c, Field f) throws Exception {
		if(NetworkListenerBean.isMessageListener(f)){
			beans.add(new NetworkListenerBean(f, MessageListener.class));
		}else if(NetworkListenerBean.isClientStateListener(f)){
			beans.add(new NetworkListenerBean(f, com.entity.anot.network.ClientConnectionListener.class));
		}else if(NetworkListenerBean.isServerConnectionsListener(f)){
			beans.add(new NetworkListenerBean(f, ServerConnectionsListener.class));
		}
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance) throws Exception {
		for(NetworkListenerBean bean:beans){			
			if(bean.isMessageListener()){
				NetworkMessageListener listener=(NetworkMessageListener) bean.instance();
				bean.getField().set(instance, listener);
				listener.setEntity(instance);
				log.info("Adding MessageListener "+listener+" on "+instance);
				app.getNet().addMsgListener(listener);
			}else if(bean.isClientStateListener()){
				ClientStateListener listener=(ClientStateListener) bean.getValueField(instance);
				app.getNet().addConnectionListener(listener);
				log.info("Adding ClientStateListener "+listener+" on "+instance);
			}else if(bean.isServerConnectionsListener()){
				ConnectionListener listener=(ConnectionListener) bean.getValueField(instance);
				app.getNet().addConnectionListener(listener);
				log.info("Adding ConnectionListener "+listener+" on "+instance);
			}
		}
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance) throws Exception {		
		for(NetworkListenerBean bean:beans){			
			if(bean.isMessageListener()){
				NetworkMessageListener listener=(NetworkMessageListener) bean.getValueField(instance);
				app.getNet().removeMsgListener(listener);
				log.info("Removing MessageListener "+listener+" on "+instance);
			}else if(bean.isClientStateListener()){
				ClientStateListener listener=(ClientStateListener) bean.getValueField(instance);
				app.getNet().removeConnectionListener(listener);
				log.info("Removing ClientStateListener "+listener+" on "+instance);
			}else if(bean.isServerConnectionsListener()){
				ConnectionListener listener=(ConnectionListener) bean.getValueField(instance);
				app.getNet().removeConnectionListener(listener);
				log.info("Removing ConnectionListener "+listener+" on "+instance);
			}
		}
	}

	@Override
	public void onInstance(IEntity item, IBuilder builder, Object[] params) throws Exception {
		
		
	}
}
