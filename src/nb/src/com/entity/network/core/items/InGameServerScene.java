package com.entity.network.core.items;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.network.ServerConnectionsListener;
import com.entity.core.EntityGame;
import com.entity.core.builders.SceneBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.listeners.InGameServerMessageListener;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.service.NetWorldService;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;


@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class InGameServerScene<T extends InGameServerMessageListener, W extends NetWorld, S extends NetWorldService, G extends EntityGame> extends Scene<G>{

	
	@ServerConnectionsListener
	public ConnectionListener cnnListener=new ConnectionListener() {
		@Override
		public void connectionAdded(Server server, HostedConnection cnn) {
			log.warning("Client "+cnn.getId()+" connected with IP "+cnn.getAddress()+" and the game has started. Kicking");		
			cnn.close("Game has stared");
		}
		
		@Override
		public void connectionRemoved(Server server, HostedConnection cnn) {
			/*NetPlayerDAO player= world.getDao().getService().getPlayerByConnection(cnn);
			if(player!=null){
				log.severe("Player quit: "+player.getId());
			}else{
				log.severe("Player quit and not found in world!!");
			}*/
			if(!server.hasConnections()){
                            getService().setPlayer(null);
                            getService().setWorld(null);
				showLobby();
			}
		}
	};


	@Override
	public void onLoadScene() throws Exception{

	}

	public abstract W getWorld();
	public abstract S getService();
	public abstract T getListener();
	public abstract void showLobby();
	
}
