package com.entity.network.core.items;

import java.util.logging.Logger;

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
import com.entity.network.core.listeners.LobbyClientMessageListener;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.entity.network.core.msg.MsgStartGame;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;

@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class LobbyClientScene<T extends LobbyClientMessageListener, W extends NetWorld, P extends NetPlayer> extends Scene implements ClientStateListener   {
	
	@MessageListener
	private T listener;

	@Persistable(fileName="player", newOnNull=true, onNewCallback="initPlayerName", onNewSave=true)
	public String playerName;


	@Override
	public void onPreInject(IBuilder builder) throws Exception {
		if(!getApp().getNet().isWorldSelected()){
			//Si no hay world selected, es otro player(no root) por lo que hay que conectarse
			Network opts=getApp().getNet().getNetworkOptions();
			int port=getApp().getNet().getPort();
			log.fine("Connecting to... "+getApp().getNet().getIp()+":"+port);
			getApp().getNet().setNetwork(com.jme3.network.Network.connectToServer(opts.gameName(), opts.version(), EntityManager.getGame().getNet().getIp(), port));				
		}
		//El listener no se ha anyadido en el worldScene
		getApp().getNet().getClient().addClientStateListener(this);
	}

	@Override
	public void onLoadScene() throws Exception{
		if(!getApp().getNet().getClient().isConnected()){			
			getApp().getNet().getClient().start();
			log.fine("Starting client...");
		}
	}

	@Override
	public void clientConnected(Client client) {
		NetPlayer playerTmp=new NetPlayer();
		playerTmp.setId(playerName);
		client.send(new MsgOnNewPlayer(playerTmp));
	}

	@Override
	public void clientDisconnected(Client paramClient, DisconnectInfo paramDisconnectInfo) {
		log.fine("Desconectado!!!! "+paramDisconnectInfo.reason);
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
	
	
	public String initPlayerName(){
		return "Player2";
	}
}
