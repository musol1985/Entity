package com.entity.network.core.items;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Task;
import com.entity.anot.network.ServerConnectionsListener;
import com.entity.core.EntityGame;
import com.entity.core.builders.SceneBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.listeners.InGameServerMessageListener;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.msg.MsgOnStartGame;
import com.entity.network.core.service.impl.ServerNetWorldService;
import com.entity.network.core.tasks.NetWorldPersistTask;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;


@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class InGameServerScene<T extends InGameServerMessageListener, W extends NetWorld, S extends ServerNetWorldService, G extends EntityGame> extends Scene<G> implements IWorldInGameScene<W,S>{
	
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
			NetPlayerDAO player= getService().getPlayerByConnection(cnn);
			player.setCnn(null);
			if(!server.hasConnections()){
				getService().getWorldDAO().reset();
                            getService().setPlayer(null);
                            getService().setWorld(null);
				showLobby();
			}
		}
	};


	@Override
	public void onLoadScene() throws Exception{
            //If is new world, preload positions of the players
            if(getService().getWorld().getDao().isCreated()){
                getService().preload();
            }   
            getService().getWorld().getDao().setCreated(false);
            MsgOnStartGame start=new MsgOnStartGame(getService().getWorld().getDao());
            start.send();		
            log.info("Starting world "+getService().getWorld().getDao().getId());
            
            
            getService().initWorld();
            onLoadRemotePlayers();
	}



	public abstract void showLobby();



	public abstract W getWorld();
	public abstract S getService();
	public abstract T getListener();
        public abstract void onLoadRemotePlayers() throws Exception;
	
}
