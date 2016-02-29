package com.entity.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.network.ClientConnectionListener;
import com.entity.anot.network.MessageListener;
import com.entity.anot.network.ServerConnectionsListener;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.jme3.network.ConnectionListener;

public class NetworkListenerBean extends AnnotationFieldBean{
	
	public NetworkListenerBean(Field f, Class<? extends Annotation> anot)throws Exception{
		super(f, anot);		
	}

	
	public static boolean isMessageListener(Field f){
		return EntityManager.isAnnotationPresent(MessageListener.class,f);
	}
	
	public static boolean isClientStateListener(Field f){
		return EntityManager.isAnnotationPresent(ClientConnectionListener.class,f);
	}
	
	public static boolean isServerConnectionsListener(Field f){
		return EntityManager.isAnnotationPresent(ServerConnectionsListener.class,f);
	}
	
	public boolean isMessageListener(){
		return isMessageListener(f);
	}
	
	public boolean isClientStateListener(){
		return isClientStateListener(f);
	}
	
	public boolean isServerConnectionsListener(){
		return isServerConnectionsListener(f);
	}
	
	public NetworkMessageListener getNewMessageListener()throws Exception{
		return (NetworkMessageListener) f.getType().newInstance();
	}
	
	public com.jme3.network.ClientStateListener getNewClientStateListener()throws Exception{
		return (com.jme3.network.ClientStateListener) f.getType().newInstance();
	}
	
	public ConnectionListener  getNewServerConnectionsListener()throws Exception{
		return (ConnectionListener ) f.getType().newInstance();
	}
}

