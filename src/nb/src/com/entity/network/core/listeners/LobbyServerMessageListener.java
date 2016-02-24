package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.core.EntityManager;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.items.LobbyServerScene;
import com.entity.network.core.msg.MsgCreateWorld;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.entity.network.core.msg.MsgOnStartGame;
import com.entity.network.core.msg.MsgOnWorldCreatedSelected;
import com.entity.network.core.msg.MsgSelectWorld;
import com.entity.network.core.msg.MsgStartGame;
import com.jme3.network.HostedConnection;

public class LobbyServerMessageListener extends NetworkMessageListener<LobbyServerScene>{

	public void onNewPlayer(MsgOnNewPlayer msg, HostedConnection cnn)throws Exception{
		log.fine("On new player "+msg.nickname+"("+cnn.getId()+"/"+cnn.getAddress()+")");
		
		if(!getEntity().canPlayersJoin()){
			log.warning("No world selected");
			cnn.close("No world selected");
			return ;
		}
		
		NetWorld w=getEntity().getWorld();
		
		
		if(w.isNewCreated()){
			log.fine("The world is new");
			//TODO comprobar si deja mas joins, de momento cerrado
			if(w.getPlayers().size()==0 && !w.getPlayerCreator().equals(msg.nickname)){
				log.warning("Admin is not connected. Sending the match is not ready");
				//El admin no esta todavia
				cnn.close("The match is not ready");
			}else{
				NetPlayer player=w.getNewPlayer();
				player.setCnn(cnn);
				player.setId(msg.nickname);	
				if(w.getPlayerCreator().equals(msg.nickname)){
					player.setAdmin(true);
					msg.admin=true;
					log.fine("Player "+msg.nickname+" is owner of the world");
				}
				
				w.getPlayers().put(msg.nickname, player);
				
				broadCast(cnn, msg, false);
				getEntity().onPlayerJoined(player);
			}
		}else{
			log.fine("The world is loaded");
			NetPlayer player=w.getNetPlayerById(msg.nickname);
			if(player!=null){
				if(player.isConnected()){
					cnn.close("Player with nickname "+msg.nickname+" already joined to game.");
					log.warning("Player with nickname "+msg.nickname+" already joined to game.");
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
				log.warning("Player with nickname "+msg.nickname+" not in the original game.");
			}
		}
	}
	
	public void onCreateWorld(MsgCreateWorld msg, HostedConnection cnn)throws Exception{
		log.fine("onCreateWorld "+msg.world.getId());
		if(getEntity().getWorlds().containsKey(msg.world.getId())){
			cnn.send(new MsgOnWorldCreatedSelected(true, true));
			log.warning("World "+msg.world.getId()+" already exists");
		}else{
			log.fine("World "+msg.world.getId()+" created!");
			getEntity().getWorlds().put(msg.world.getId(), msg.world);

			getEntity().setWorld(msg.world);
			cnn.send(new MsgOnWorldCreatedSelected(false, true));
			
			EntityManager.savePersistableFieldName(getEntity(), "worlds");
		}
	}
	
	public void onSelectWorld(MsgSelectWorld msg, HostedConnection cnn)throws Exception{
		log.fine("onSelectWorld "+msg.world);
		if(!getEntity().getWorlds().containsKey(msg.world)){
			log.warning("World "+msg.world+" doesn't exists");
			cnn.send(new MsgOnWorldCreatedSelected(true, false));
		}else{			
			getEntity().setWorld((NetWorld) getEntity().getWorlds().get(msg.world));
			cnn.send(new MsgOnWorldCreatedSelected(false, false));
			log.fine("World "+msg.world+" selected");
		}
	}
	
	public void onStartWorld(MsgStartGame msg, HostedConnection cnn)throws Exception{
		log.fine("onStartWorld");
		if(!getEntity().isWorldSelected()){
			log.warning("No world selected. Sending error");
			cnn.send(new MsgOnWorldCreatedSelected(true, false));
		}else{
			MsgOnStartGame start=new MsgOnStartGame(getEntity().getWorld().getId(), getEntity().getWorld().getTimestamp());
			broadCast(cnn, start, false);
			log.fine("Starting world");
		}		
	}
}
