package com.entity.network.core.items;

import java.util.logging.Logger;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Persistable;
import com.entity.anot.network.ClientStateListener;
import com.entity.anot.network.MessageListener;
import com.entity.anot.network.Network;
import com.entity.anot.network.WorldService;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.builders.SceneBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.listeners.LobbyClientMessageListener;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.entity.network.core.msg.MsgStartGame;
import com.entity.network.core.service.NetWorldService;
import com.jme3.network.Client;

@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class LobbyClientScene<T extends LobbyClientMessageListener, S extends NetWorldService, P extends NetPlayerDAO> extends Scene  {
	
	@MessageListener
	private T listener;

	
	@Persistable(fileName="player.conf", newOnNull=true, onNewCallback="initPlayerName", onNewSave=true)
	public String playerName;
	
	@WorldService
	public S service;

	@ClientStateListener
	public com.jme3.network.ClientStateListener stateListener=new com.jme3.network.ClientStateListener(){
		@Override
		public void clientConnected(Client client) {
			onConnected(client);
		}

		@Override
		public void clientDisconnected(Client paramClient, DisconnectInfo paramDisconnectInfo) {
			log.info("Desconectado!!!! "+paramDisconnectInfo.reason);
		}		
	}; 

	@Override
	public void onPreInject(IBuilder builder) throws Exception {
		if(!service.isWorldSelected()){
			//Si no hay world selected, es otro player(no root) por lo que hay que conectarse
			Network opts=getApp().getNet().getNetworkOptions();
			int port=getApp().getNet().getPort();
			log.info("Connecting to... "+getApp().getNet().getIp()+":"+port);
			getApp().getNet().setNetwork(com.jme3.network.Network.connectToServer(opts.gameName(), opts.version(), EntityManager.getGame().getNet().getIp(), port));				
		}
	}

	@Override
	public void onLoadScene() throws Exception{
		if(!getApp().getNet().getClient().isConnected()){			
			getApp().getNet().getClient().start();
			log.info("Starting client...");
		}else{
			onConnected(getApp().getNet().getClient());
		}
	}
	
	public void onConnected(Client client){
		NetPlayerDAO playerTmp=new NetPlayerDAO();
		playerTmp.setId(playerName);
		client.send(new MsgOnNewPlayer(playerTmp));
        log.info("OnClienConnect. Sending playerTmp");
	}


	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	
	public void startGame(){
		MsgStartGame msg=new MsgStartGame();
		msg.send();
	}
	
	public abstract void onPlayerReady(P player);
	/**
	 * When admin starts game, show Scene <? extends InGameClientScene>
	 * States->
	 * 		LobbyGame(Players can join, admin sets up the world)
	 * 		StartGame(admin start the game)
	 * 		LoadingGame(load game)
	 * 		send gameLoaded
	 * 		When server has all players gameLoaded sends beginGame
	 */
	public abstract void onStartGame();
	
	
	public String initPlayerName(){
		return "Player2";
	}
}
