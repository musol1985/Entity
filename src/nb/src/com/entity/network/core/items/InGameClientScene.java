package com.entity.network.core.items;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Entity;
import com.entity.anot.network.ClientConnectionListener;
import com.entity.anot.network.MessageListener;
import com.entity.anot.network.WorldService;
import com.entity.core.builders.SceneBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.listeners.InGameClientMessageListener;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.service.impl.ClientNetWorldService;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;


@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class InGameClientScene<T extends InGameClientMessageListener, W extends NetWorld, P extends NetPlayer, S extends ClientNetWorldService> extends Scene implements IWorldInGameScene<W,S>{
    private boolean loaded;
	
	@ClientConnectionListener
	public ClientStateListener stateListener=new ClientStateListener(){
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
		getService().updatePlayerLocation(getPlayer().getDao().getPosition());
	}


	public abstract W getWorld();
	public abstract P getPlayer();
	public abstract S getService();
	public abstract T getListener();

}
