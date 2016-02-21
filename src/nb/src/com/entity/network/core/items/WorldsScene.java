package com.entity.network.core.items;

import java.util.HashMap;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Persistable;
import com.entity.anot.network.MessageListener;
import com.entity.anot.network.Network;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.builders.SceneBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.listeners.WorldsMessageListener;

@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class WorldsScene<T extends WorldsMessageListener, W extends NetWorld, P extends NetPlayer> extends Scene{
	@MessageListener
	private T listener;
	@Persistable(fileName="player")
	private String playerName;

	@Override
	public void onPreInject(IBuilder builder) throws Exception {
		Network opts=EntityManager.getGame().getNet().getNetworkOptions();
		int port=EntityManager.getGame().getNet().getPort();
		System.out.println("Connecting to... "+EntityManager.getGame().getNet().getIp()+":"+port);
		EntityManager.getGame().getNet().setNetwork(com.jme3.network.Network.connectToServer(opts.gameName(), opts.version(), EntityManager.getGame().getNet().getIp(), port));				
	}

	public void loadScene() throws Exception{
		EntityManager.getGame().getNet().getClient().start();
	}
	
	
	public NetWorld createWorld(){
		NetWorld world=new NetWorld<NetPlayer>(new HashMap(), "pruebas", System.currentTimeMillis());
		return world;
	}
	
	public abstract void showLobby();
		
	public String getPlayerName(){
		return playerName;
	}
}
