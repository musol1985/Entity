package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.core.EntityManager;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.items.LobbyClientScene;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.jme3.network.HostedConnection;

public class LobbyClientMessageListener extends NetworkMessageListener<LobbyClientScene>{
	
	
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
			EntityManager.getGame().getNet().setPlayer(player);
	}

}
