package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.core.EntityManager;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldDAO;
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
		log.info("On new player "+msg.player.getId()+"("+cnn.getId()+"/"+cnn.getAddress()+")");
		
		if(!getEntity().canPlayersJoin()){
			log.warning("No world selected");
			cnn.close("No world selected");
			return ;
		}
		
		NetWorldDAO w=getEntity().getWorld();
		
		
		if(w.isCreated()){
			log.info("The world is new");
			//TODO comprobar si deja mas joins, de momento cerrado
			if(w.getPlayers().size()==0 && !w.getPlayerCreator().equals(msg.player.getId())){
				log.warning("Admin is not connected. Sending the match is not ready");
				//El admin no esta todavia
				cnn.close("The match is not ready");
			}else{
				if(w.getNetPlayerById(msg.player.getId())==null){
					NetPlayerDAO player=w.getService().createNewPlayer(msg.player.getId());
					player.setCnn(cnn);

					if(w.getPlayerCreator().equals(player.getId())){
						player.setAdmin(true);
						log.info("Player "+msg.player.getId()+" is owner of the world");
					}
					
					w.getPlayers().put(player.getId(), player);
					
					broadCast(cnn, msg, false);
					getEntity().onPlayerJoined(player);
				}else{
					cnn.close("Player with nickname "+msg.player.getId()+" already joined to game.");
					log.warning("Player with nickname "+msg.player.getId()+" already joined to game.");
				}			
			}
		}else{
			log.info("The world is loaded");
			NetPlayerDAO player=w.getNetPlayerById(msg.player.getId());
			if(player!=null){
				if(player.isConnected()){
					cnn.close("Player with nickname "+player.getId()+" already joined to game.");
					log.warning("Player with nickname "+player.getId()+" already joined to game.");
				}else{
					player.setCnn(cnn);
					if(w.getPlayerCreator().equals(player.getId())){
						player.setAdmin(true);
					}
					broadCast(cnn, msg, false);
					getEntity().onPlayerJoined(player);
				}
			}else{
				cnn.close("Player with nickname "+msg.player.getId()+" not in the original game.");
				log.warning("Player with nickname "+msg.player.getId()+" not in the original game.");
			}
		}
	}
	
	public void onCreateWorld(MsgCreateWorld msg, HostedConnection cnn)throws Exception{
		log.info("onCreateWorld "+msg.world.getId());
		if(getEntity().getWorlds().containsKey(msg.world.getId())){
			cnn.send(new MsgOnWorldCreatedSelected(true, true));
			log.warning("World "+msg.world.getId()+" already exists");
		}else{
			log.info("World "+msg.world.getId()+" created!");
			getEntity().getWorlds().put(msg.world.getId(), msg.world);

			getEntity().setWorld(msg.world);
			cnn.send(new MsgOnWorldCreatedSelected(false, true));
			
			//EntityManager.savePersistableFieldName(getEntity(), "worlds");
		}
	}
	
	public void onSelectWorld(MsgSelectWorld msg, HostedConnection cnn)throws Exception{
		log.info("onSelectWorld "+msg.world);
		if(!getEntity().getWorlds().containsKey(msg.world)){
			log.warning("World "+msg.world+" doesn't exists");
			cnn.send(new MsgOnWorldCreatedSelected(true, false));
		}else{						
			NetWorldDAO w=(NetWorldDAO) getEntity().getWorlds().get(msg.world);
			w.init();
			getEntity().setWorld(w);
			cnn.send(new MsgOnWorldCreatedSelected(false, false));
			log.info("World "+msg.world+" selected");
		}
	}
	
	public void onStartWorld(MsgStartGame msg, HostedConnection cnn)throws Exception{
		log.info("onStartWorld");
		if(!getEntity().isWorldSelected()){
			log.warning("No world selected. Sending error");
			cnn.send(new MsgOnWorldCreatedSelected(true, false));
		}else{
			NetWorldDAO world=getEntity().getWorld();
			//If is new world, preload positions of the players
			if(world.isCreated()){
				world.getService().preload();
			}
			MsgOnStartGame start=new MsgOnStartGame(world);
			broadCast(cnn, start, false);
			log.info("Starting world "+world.getId());
		}		
	}
}
