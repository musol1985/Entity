package com.entity.network.core.items;

import java.util.HashMap;

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
import com.entity.network.core.listeners.LobbyClientMessageListener;
import com.entity.network.core.msg.MsgCreateWorld;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.entity.network.core.msg.MsgOnStartGame;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;

@BuilderDefinition(builderClass=LobbyBuilder.class)
public abstract class LobbyClientScene<T extends LobbyClientMessageListener, W extends NetWorld, P extends NetPlayer> extends Scene implements ClientStateListener   {
	@MessageListener
	private T listener;

	@Persistable(fileName="player")
	private String playerName;
	
	private NetPlayer player;

	@Override
	public void onPreInject(IBuilder builder) throws Exception {
		Network opts=EntityManager.getGame().getNet().getNetworkOptions();
		int port=EntityManager.getGame().getNet().getPort();
		System.out.println("Connecting to... "+EntityManager.getGame().getNet().getIp()+":"+port);
		EntityManager.getGame().getNet().setNetwork(com.jme3.network.Network.connectToServer(opts.gameName(), opts.version(), EntityManager.getGame().getNet().getIp(), port));		
		EntityManager.getGame().getNet().getClient().addClientStateListener(this);
	}

	@Override
	public void loadScene() throws Exception{
		EntityManager.getGame().getNet().getClient().start();
	}

	@Override
	public void clientConnected(Client client) {
		client.send(new MsgOnNewPlayer(playerName, false));
	}

	@Override
	public void clientDisconnected(Client paramClient, DisconnectInfo paramDisconnectInfo) {
		System.out.println("Desconectado!!!! "+paramDisconnectInfo.reason);
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public NetPlayer getPlayer() {
		return player;
	}

	public void setPlayer(NetPlayer player) {
		this.player = player;
	}
	
	public void startGame(){
		MsgOnStartGame msg=new MsgOnStartGame(System.currentTimeMillis());
		msg.send();
	}
	
	public abstract void onPlayerReady(P player);
	
	
	public NetWorld createWorld(){
		NetWorld world=new NetWorld<NetPlayer>(new HashMap(), "pruebas", System.currentTimeMillis());
		new MsgCreateWorld(world).send();
		return world;
	}
}
