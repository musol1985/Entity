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
	
	private boolean adminConnected;

	@Override
	public void connectionAdded(Server server, HostedConnection cnn) {
		if(server.getConnections().size()>getMaxPlayers()){
			cnn.close("Max players");
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
	

	public boolean isAdminConnected() {
		return adminConnected;
	}

	public void setAdminConnected(boolean adminConnected) {
		this.adminConnected = adminConnected;
	}

	public abstract void onPlayerJoined(P player);
}
