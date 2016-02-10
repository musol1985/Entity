package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.core.EntityManager;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.items.LobbyServerScene;
import com.entity.network.core.msg.MsgCreateWorld;
import com.entity.network.core.msg.MsgListWorlds;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.entity.network.core.msg.MsgOnPlayerReady;
import com.entity.network.core.msg.MsgOnWorldCreated;
import com.entity.network.core.msg.MsgSelectWorld;
import com.jme3.network.HostedConnection;

public class LobbyServerMessageListener extends NetworkMessageListener<LobbyServerScene>{

	public void onNewPlayer(MsgOnNewPlayer msg, HostedConnection cnn)throws Exception{
		NetWorld w=EntityManager.getGame().getNet().getWorld();
		
		if(w==null){
			if(!getEntity().isAdminConnected()){
				cnn.send(new MsgListWorlds(getEntity().getWorlds()));
			}else{
				cnn.close("No world selected");
			}
			getEntity().setAdminConnected(true);
			return ;
		}
		
		NetPlayer player=w.getNetPlayerById(msg.nickname);
		
		if(player!=null){
			if(player.isConnected()){
				cnn.close("Player with nickname "+msg.nickname+" already joined to game.");
			}else{
				player.setCnn(cnn);
				msg.admin=player.isAdmin();
				broadCast(cnn, msg, false);
				getEntity().onPlayerJoined(player);
			}
		}else{
			player=w.getNewPlayer();
			player.setCnn(cnn);
			player.setId(msg.nickname);			
			
			if(w.getPlayers().size()==0){
				player.setAdmin(true);
				msg.admin=true;
			}
		
			w.getPlayers().put(msg.nickname, player);
			
			broadCast(cnn, msg, false);
			getEntity().onPlayerJoined(player);
		}
	}
	
	public void onCreateWorld(MsgCreateWorld msg, HostedConnection cnn)throws Exception{
		if(getEntity().getWorlds().containsKey(msg.world.getId())){
			cnn.send(new MsgOnWorldCreated(true));
		}else{
			getEntity().getWorlds().put(msg.world.getId(), msg.world);
			cnn.send(new MsgOnWorldCreated(false));
		}
	}
	
	public void onSelectWorld(MsgSelectWorld msg, HostedConnection cnn)throws Exception{
		
	}
	/*
	public void onPlayerReady(MsgOnPlayerReady msg, HostedConnection cnn)throws Exception{
		NetPlayer player=getEntity().getWorld().getNetPlayerById(msg.nickname);
		if(player!=null){
			player.setReady(true);
			broadCast(cnn, msg, true);
		}else{
			throw new Exception("Player "+msg.nickname+" has not joined");
		}
	}*/
}
