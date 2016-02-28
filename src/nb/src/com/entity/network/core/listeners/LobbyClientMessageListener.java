package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.core.EntityManager;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.items.LobbyClientScene;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.entity.network.core.msg.MsgOnStartGame;
import com.jme3.network.MessageConnection;

public class LobbyClientMessageListener extends NetworkMessageListener<LobbyClientScene>{
	
	
	public void onNewPlayer(MsgOnNewPlayer msg, MessageConnection cnn)throws Exception{
		log.info("OnNewPlayer "+msg.player.getId());
		NetWorldDAO w=getEntity().service.getWorldDAO();
		NetPlayerDAO player=msg.player;
		
		if(!player.isConnected()){
			player.setCnn(cnn);
		}
		
		if(w!=null && w.getNetPlayerById(player.getId())==null){
			w.getPlayers().put(msg.player.getId(), player);
		}

		
		if(getEntity().getPlayerName().equals(player.getId())){
			getEntity().service.setPlayerDAO(player);
		}else{
			getEntity().onNewPlayer(player);
		}			
	}
	
	public void onStartWorld(MsgOnStartGame msg, MessageConnection cnn)throws Exception{
		getEntity().service.setWorldDAO(msg.world);		
		
		getEntity().onStartGame();
	}

}
