package com.entity.network.core.items;

import java.util.HashMap;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Persistable;
import com.entity.anot.network.MessageListener;
import com.entity.anot.network.Network;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.builders.SceneBuilder;
import com.entity.core.items.Scene;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.listeners.WorldsMessageListener;
import com.entity.network.core.msg.MsgCreateWorld;
import com.entity.network.core.msg.MsgListWorlds;
import com.entity.network.core.msg.MsgSelectWorld;

@BuilderDefinition(builderClass=SceneBuilder.class)
public abstract class WorldsScene<T extends WorldsMessageListener, W extends NetWorld, P extends NetPlayer, G extends EntityGame> extends Scene<G> {
	@MessageListener
	public T listener;
	@Persistable(fileName="player.conf", newOnNull=true, onNewCallback="initPlayerName", onNewSave=true)
	public String playerName;

	@Override
	public void onPreInject(IBuilder builder) throws Exception {
		Network opts=EntityManager.getGame().getNet().getNetworkOptions();
		int port=EntityManager.getGame().getNet().getPort();
		log.info("Connecting to... "+EntityManager.getGame().getNet().getIp()+":"+port);		
		EntityManager.getGame().getNet().setNetwork(com.jme3.network.Network.connectToServer(opts.gameName(), opts.version(), EntityManager.getGame().getNet().getIp(), port));		
	}

	@Override
	public void onLoadScene() throws Exception{
		EntityManager.getGame().getNet().getClient().start();
	}
	
	
	public abstract W createWorld();
	
	public void showWorldsList(MsgListWorlds<W> msg){
		//TODO show on GUI the worlds & create World(with options)		
		log.warning("You must override WorldsScene.showWorldsList!!");
		log.warning("Creating or selecting world 0 automatically");
		W world=null;
		
		if(msg.worlds.size()==0){
			log.info("No worlds created");
			//Creamos a world
			world=createWorld();
			world.setCreated(true);
			world.setPlayerCreator(getPlayerName());
			new MsgCreateWorld<W>(world).send();
		}else{
			//Select the first world
			world=msg.worlds.values().iterator().next();
			log.info("Selecting first world "+world.getId()+" created by "+world.getPlayerCreator());
			new MsgSelectWorld(world.getId()).send();			
		}		
		EntityManager.getGame().getNet().setWorld(world);
		//Select the first world
		log.info("World "+world.getId()+" selected");
	}
	
	public abstract void showLobby();
		
	public String getPlayerName(){
		return playerName;
	}
	
	public String initPlayerName(){
		return "Player1";
	}
	
}
