package com.entity.core;

import java.lang.reflect.Field;
import java.util.Set;

import org.reflections.Reflections;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.CustomInjectors;
import com.entity.anot.entities.SceneEntity;
import com.entity.anot.network.Network;
import com.entity.core.items.Scene;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

public abstract class EntityGame extends SimpleApplication{
	private Field networkField;
	
	@Override
	public void simpleInitApp() {
		try{
			EntityManager.StartEntityFramework(this);
			
			Scene autoload=null;
			for(Field f:getClass().getDeclaredFields()){
				if(f.isAnnotationPresent(Network.class)){
					 Reflections reflections = new Reflections(f.getAnnotation(Network.class).messagesPackage());
		
					 Set<Class<?>> messages=reflections.getTypesAnnotatedWith(Serializable.class);
					 
					 for(Class<?> message:messages){
						 Serializer.registerClass(message);
					 }
					 
					 networkField=f;
					 return;
				}else if(f.isAnnotationPresent(SceneEntity.class)){
					Object scene=EntityManager.instanceGeneric(f.getType());
					f.setAccessible(true);
					f.set(this, scene);
					if(f.getAnnotation(SceneEntity.class).attach())
						autoload=(Scene) scene;
				}
			}
                        
            CustomInjectors customs=getClass().getAnnotation(CustomInjectors.class);
			if(customs!=null){
				EntityManager.setCustomInjectors(customs.sceneInjectors(), customs.modelInjectors());
			}
                        
			if(autoload!=null)
				setScene(autoload);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addMessageListener(NetworkMessageListener listener)throws Exception{
		if(isNetworkGame()){
			if(networkField.get(this)==Client.class){
				Client client=(Client) networkField.get(this);
				client.addMessageListener(listener);
			}else if(networkField.get(this).getClass()==Server.class){
				Server server=(Server) networkField.get(this);
				server.addMessageListener(listener);
			}
		}
	}
	
	public void removeMessageListener(NetworkMessageListener listener)throws Exception{
		if(networkField!=null){
			networkField.setAccessible(true);
			Object net=networkField.get(this);
			if(net!=null){
				if(net instanceof Client){
					((Client) net).removeMessageListener(listener);
				}else if(net instanceof Server){
					((Server) net).removeMessageListener(listener);
				}
			}
		}
	}
	
	public Server getNetworkServer(){
		if(networkField!=null){
			try {
				return (Server)networkField.get(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Client getNetworkClient(){
		if(networkField!=null){
			try {
				return (Client)networkField.get(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public boolean isNetworkGame(){
		return networkField!=null;
	}	
	
	public void setScene(Scene scene){
		try {
			Scene current=getStateManager().getState(Scene.class);
			if(current!=null)
				current.dettach();
			
			scene.attachToParent(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
