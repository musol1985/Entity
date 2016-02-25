package com.entity.network.core.items;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Entity;
import com.entity.anot.network.ClientStateListener;
import com.entity.anot.network.MessageListener;
import com.entity.core.builders.SceneBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.listeners.InGameClientMessageListener;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.jme3.network.Client;


@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class InGameClientScene<T extends InGameClientMessageListener, W extends NetWorld, P extends NetPlayer> extends Scene implements ClientStateListener   {
	
	@MessageListener
	private T listener;
	
	@Entity
	public W world;
	@Entity
	public P player;
	
	@ClientStateListener
	public com.jme3.network.ClientStateListener stateListener=new com.jme3.network.ClientStateListener(){
		@Override
		public void clientConnected(Client client) {
			
		}

		@Override
		public void clientDisconnected(Client paramClient, DisconnectInfo paramDisconnectInfo) {
			log.info("Desconectado!!!! "+paramDisconnectInfo.reason);
		}		
	}; 


	@Override
	public void onLoadScene() throws Exception{

	}



}
