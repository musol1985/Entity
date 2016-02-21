package com.entity.network.core.items;

import java.util.HashMap;
import java.util.List;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Persistable;
import com.entity.anot.network.MessageListener;
import com.entity.anot.network.Network;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.builders.LobbyBuilder;
import com.entity.network.core.listeners.LobbyServerMessageListener;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

@BuilderDefinition(builderClass=LobbyBuilder.class)
public abstract class LobbyServerScene<T extends LobbyServerMessageListener, W extends NetWorld, P extends NetPlayer> extends Scene  implements ConnectionListener {
	@MessageListener
	private T listener;
	
	@Persistable(fileName="worlds",newOnNull=true)
	private HashMap<String, W> worlds;

	@Override
	public void connectionAdded(Server server, HostedConnection cnn) {
		if(server.getConnections().size()>getMaxPlayers()){
			cnn.close("Max players");
		}else if(server.getConnections().size()>1 && !isWorldSelected()){
			cnn.close("El admin no ha seleccionado mundo todavía");
		}
	}

	@Override
	public void connectionRemoved(Server arg0, HostedConnection arg1) {
		
	}

	@Override
	public void onPreInject(IBuilder builder) throws Exception {
		Network opts=EntityManager.getGame().getNet().getNetworkOptions();
		int port=EntityManager.getGame().getNet().getPort();
		System.out.println("Listening on... "+port);
		EntityManager.getGame().getNet().setNetwork(com.jme3.network.Network.createServer(opts.gameName(), opts.version(), port, port));
		EntityManager.getGame().getNet().getServer().addConnectionListener(this);
		
	}


	public void loadScene() throws Exception{
		System.out.println("Starting server... ");
		EntityManager.getGame().getNet().getServer().start();
	}
	
	private int getMaxPlayers(){
		return 99;
	}
	
	
	public HashMap<String, W>  getWorlds() {
		return worlds;
	}
	

	public boolean isWorldSelected(){
		return getWorld()!=null;
	}
	
	/**
	 * Comprueba si world!=null(hay world seleccionado/creado)
	 * @return
	 */
	public boolean canPlayersJoin(){
		return isWorldSelected();
	}

	public W getWorld() {
		return (W) EntityManager.getGame().getNet().getWorld();
	}

	public void setWorld(W world) {
		EntityManager.getGame().getNet().setWorld(world);
	}

	public abstract void onPlayerJoined(P player);
}
