package com.entity.network.core.items;

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

	@Persistable(fileName="player")
	private String playerName;


	@Override
	public void onPreInject(IBuilder builder) throws Exception {
		if(!getApp().getNet().isWorldSelected()){
			//Si no hay world selected, es otro player(no root) por lo que hay que conectarse
			Network opts=getApp().getNet().getNetworkOptions();
			int port=getApp().getNet().getPort();
			System.out.println("Connecting to... "+getApp().getNet().getIp()+":"+port);
			getApp().getNet().setNetwork(com.jme3.network.Network.connectToServer(opts.gameName(), opts.version(), EntityManager.getGame().getNet().getIp(), port));				
		}
		//El listener no se ha añadido en el worldScene
		getApp().getNet().getClient().addClientStateListener(this);
	}

	public void loadScene() throws Exception{
		getApp().getNet().getClient().start();
	}

	@Override
	public void clientConnected(Client client) {
		client.send(new MsgOnNewPlayer(playerName, getApp().getNet().isWorldSelected()));
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

	
	public void startGame(){
		MsgStartGame msg=new MsgStartGame(System.currentTimeMillis());
		msg.send();
	}
	
	public abstract void onPlayerReady(P player);
	
	

}
