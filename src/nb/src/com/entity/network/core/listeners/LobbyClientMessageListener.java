package com.entity.network.core.listeners;

import java.util.HashMap;

import com.entity.adapters.NetworkMessageListener;
import com.entity.core.EntityManager;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.items.LobbyClientScene;
import com.entity.network.core.msg.MsgCreateWorld;
import com.entity.network.core.msg.MsgListWorlds;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.entity.network.core.msg.MsgOnPlayerReady;
import com.entity.network.core.msg.MsgOnWorldCreated;
import com.entity.network.core.msg.MsgSelectWorld;
import com.jme3.network.HostedConnection;

public class LobbyClientMessageListener extends NetworkMessageListener<LobbyClientScene>{
	
	public void onListWorlds(MsgListWorlds msg, HostedConnection cnn)throws Exception{
		//TODO show on GUI the worlds & create World
		NetWorld world=null;
		
		if(msg.worlds.size()==0){
			world=getEntity().createWorld();
		}else{
			world=msg.worlds.get(0);
			cnn.send(new MsgSelectWorld(world.getId()));
			
		}
		EntityManager.getGame().getNet().setWorld(world);
	}
	
	public void onWorldCreated(MsgOnWorldCreated msg, HostedConnection cnn) throws Exception{
		if(msg.error){
			throw new RuntimeException("El mundo ya existe....");
		}else{
			new MsgOnNewPlayer(getEntity().getPlayerName(), true).send();
		}
	}
	
	public void onNewPlayer(MsgOnNewPlayer msg, HostedConnection cnn)throws Exception{
		NetWorld w=EntityManager.getGame().getNet().getWorld();
		NetPlayer player=w.getNetPlayerById(msg.nickname);
		
		if(player!=null){
			if(!player.isConnected()){
				player.setCnn(cnn);
			}
		}else{
			player=w.getNewPlayer();
			player.setCnn(cnn);
			player.setId(msg.nickname);
			player.setAdmin(msg.admin);
			w.getPlayers().put(msg.nickname, player);
		}
		
		if(getEntity().getPlayerName().equals(player.getId()))
			getEntity().setPlayer(player);
	}
/*
	
	public void onPlayerReady(MsgOnPlayerReady msg, HostedConnection cnn)throws Exception{
		NetPlayer player=getEntity().getWorld().getNetPlayerById(msg.nickname);
		if(player!=null){
			player.setReady(true);
		}else{
			throw new Exception("Player "+msg.nickname+" has not joined");
		}
	}*/
}
