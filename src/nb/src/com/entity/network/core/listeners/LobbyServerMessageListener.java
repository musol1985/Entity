package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.items.LobbyServerScene;
import com.entity.network.core.msg.MsgCreateWorld;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.entity.network.core.msg.MsgOnWorldCreatedSelected;
import com.entity.network.core.msg.MsgSelectWorld;
import com.entity.network.core.msg.MsgStartGame;
import com.jme3.network.HostedConnection;

public class LobbyServerMessageListener extends NetworkMessageListener<LobbyServerScene>{

	public void onNewPlayer(MsgOnNewPlayer msg, HostedConnection cnn)throws Exception{
		if(!getEntity().canPlayersJoin()){
			cnn.close("No world selected");
			return ;
		}
		
		NetWorld w=getEntity().getWorld();
		
		
		if(w.isNewCreated()){
			//TODO comprobar si deja mas joins, de momento cerrado
			if(w.getPlayers().size()==0 && !w.getPlayerCreator().equals(msg.nickname)){
				//El admin no esta todav�a
				cnn.close("Admin not connected");
			}else{
				NetPlayer player=w.getNewPlayer();
				player.setCnn(cnn);
				player.setId(msg.nickname);	
				if(w.getPlayerCreator().equals(msg.nickname)){
					player.setAdmin(true);
					msg.admin=true;
				}
				
				w.getPlayers().put(msg.nickname, player);
				
				broadCast(cnn, msg, false);
				getEntity().onPlayerJoined(player);
			}
		}else{
			NetPlayer player=w.getNetPlayerById(msg.nickname);
			if(player!=null){
				if(player.isConnected()){
					cnn.close("Player with nickname "+msg.nickname+" already joined to game.");
				}else{
					player.setCnn(cnn);
					if(w.getPlayerCreator().equals(msg.nickname)){
						player.setAdmin(true);
						msg.admin=true;
					}
					broadCast(cnn, msg, false);
					getEntity().onPlayerJoined(player);
				}
			}else{
				cnn.close("Player with nickname "+msg.nickname+" not in the original game.");
			}
		}
	}
	
	public void onCreateWorld(MsgCreateWorld msg, HostedConnection cnn)throws Exception{
		if(getEntity().getWorlds().containsKey(msg.world.getId())){
			cnn.send(new MsgOnWorldCreatedSelected(true, true));
		}else{
			getEntity().getWorlds().put(msg.world.getId(), msg.world);
			//TODO guardar worlds en fs
			getEntity().setWorld(msg.world);
			cnn.send(new MsgOnWorldCreatedSelected(false, true));
		}
	}
	
	public void onSelectWorld(MsgSelectWorld msg, HostedConnection cnn)throws Exception{
		if(getEntity().getWorlds().containsKey(msg.world)){
			cnn.send(new MsgOnWorldCreatedSelected(true, false));
		}else{
			getEntity().setWorld((NetWorld) getEntity().getWorlds().get(msg.world));
			cnn.send(new MsgOnWorldCreatedSelected(false, false));
		}
	}
	
	public void onStartGame(MsgStartGame msg, HostedConnection cnn)throws Exception{
		
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
