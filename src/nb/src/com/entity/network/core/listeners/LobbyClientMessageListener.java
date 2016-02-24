package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.core.EntityManager;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.items.LobbyClientScene;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.jme3.network.MessageConnection;

public class LobbyClientMessageListener extends NetworkMessageListener<LobbyClientScene>{
	
	
	public void onNewPlayer(MsgOnNewPlayer msg, MessageConnection cnn)throws Exception{
		log.info("OnNewPlayer "+msg.player.getId());
		NetWorld w=EntityManager.getGame().getNet().getWorld();
		NetPlayer player=msg.player;
		
		if(!player.isConnected()){
			player.setCnn(cnn);
		}
		
		if(w!=null && w.getNetPlayerById(player.getId())==null){
			w.getPlayers().put(msg.player.getId(), player);
		}

		
		if(getEntity().getPlayerName().equals(player.getId()))
			EntityManager.getGame().getNet().setPlayer(player);
	}

}
