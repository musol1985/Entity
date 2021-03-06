package com.entity.network.core.items;

import java.util.HashMap;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Persistable;
import com.entity.anot.network.Network;
import com.entity.anot.network.ServerConnectionsListener;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.builders.SceneBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.listeners.LobbyServerMessageListener;
import com.entity.network.core.msg.MsgListWorlds;
import com.entity.network.core.service.impl.ServerNetWorldService;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class LobbyServerScene<T extends LobbyServerMessageListener, S extends ServerNetWorldService, W extends NetWorldDAO, P extends NetPlayerDAO, G extends EntityGame> extends Scene<G>  {
	@Persistable(fileName="worlds",newOnNull=true)
	public HashMap<String, W> worlds;

	
	@ServerConnectionsListener
	public ConnectionListener cnnListener=new ConnectionListener() {
		@Override
		public void connectionAdded(Server server, HostedConnection cnn) {
			log.info("Client "+cnn.getId()+" connected with IP "+cnn.getAddress());		
			if(server.getConnections().size()>getMaxPlayers()){
				log.info("Client "+cnn.getId()+" cannot be joined. MaxPlayers");		
				cnn.close("Max players");
			}else if(server.getConnections().size()>1 && !isWorldSelected()){
				log.info("Owner hasn't selected a world.");	
				cnn.close("Owner hasn't selected a world.");
			}else if(server.getConnections().size()==1){
				log.info("Client "+cnn.getId()+" is owner. Sending the world list: "+worlds.size());	
				cnn.send(new MsgListWorlds(worlds));
			}
		}
		
		@Override
		public void connectionRemoved(Server server, HostedConnection arg1) {
			if(server.getConnections().size()==0){
				log.info("Owner has exited, setting world to null");	
				getService().setWorldDAO(null);
			}
		}
	};

	@Override
	public void onPreInject(IBuilder builder, Object[] params) throws Exception {
		if(EntityManager.getGame().getNet().getServer()==null){
			Network opts=EntityManager.getGame().getNet().getNetworkOptions();
			int port=EntityManager.getGame().getNet().getPort();
			log.info("Listening on... "+port);
			EntityManager.getGame().getNet().setNetwork(com.jme3.network.Network.createServer(opts.gameName(), opts.version(), port, port));
		}
	}

	@Override
	public void onLoadScene() throws Exception{
		if(!EntityManager.getGame().getNet().getServer().isRunning()){
			log.info("Starting server... ");
			EntityManager.getGame().getNet().getServer().start();
		}else{
			log.info("Server already running... ");	
		}
	}
	
	private int getMaxPlayers(){
		return 99;
	}
	
	
	public HashMap<String, W>  getWorlds() {
		return worlds;
	}
	

	public boolean isWorldSelected(){
		return getService().getWorldDAO()!=null;
	}
	
	/**
	 * Comprueba si world!=null(hay world seleccionado/creado)
	 * @return
	 */
	public boolean canPlayersJoin(){
		return isWorldSelected();
	}	
	
	public abstract S getService();
	public abstract T getListener();
	public abstract void onPlayerJoined(P player);
    public abstract void onStarGame();
}
