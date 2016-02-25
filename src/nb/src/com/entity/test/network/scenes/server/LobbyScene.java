package com.entity.test.network.scenes.server;

import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.items.LobbyServerScene;
import com.entity.network.core.listeners.LobbyServerMessageListener;

public class LobbyScene extends LobbyServerScene<LobbyServerMessageListener, NetWorldDAO, NetPlayerDAO> {

	@Override
	public void onPlayerJoined(NetPlayerDAO player) {
		System.out.println("Player joined! "+player.getId());
		/*if(getWorld().getPlayers().size()>1){
			startGame();
		}*/
	}

}
