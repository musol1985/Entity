package com.entity.network.core.items;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Entity;
import com.entity.anot.network.ClientStateListener;
import com.entity.anot.network.MessageListener;
import com.entity.anot.network.ServerConnectionsListener;
import com.entity.core.builders.SceneBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.listeners.InGameServerMessageListener;
import com.entity.network.core.models.NetWorld;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;


@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class InGameServerScene<T extends InGameServerMessageListener, W extends NetWorld> extends Scene implements ClientStateListener   {
	
	@MessageListener
	public T listener;
	
	@Entity
	public W world;
	
	
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
		}
	};


	@Override
	public void onLoadScene() throws Exception{
		
	}
}
