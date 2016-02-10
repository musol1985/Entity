package com.entity.test.network.scenes.server;

import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.items.LobbyServerScene;
import com.entity.network.core.listeners.LobbyServerMessageListener;

public class LobbyScene extends LobbyServerScene<LobbyServerMessageListener, NetWorld, NetPlayer> {

	@Override
	public void onPlayerJoined(NetPlayer player) {
		System.out.println("Player joined! "+player.getId());
		/*if(getWorld().getPlayers().size()>1){
			startGame();
		}*/
	}

}
