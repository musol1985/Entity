package com.entity.test.network.scenes.client;

import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.items.LobbyClientScene;
import com.entity.network.core.listeners.LobbyClientMessageListener;

public class LobbyScene extends LobbyClientScene<LobbyClientMessageListener, NetWorld, NetPlayer> {

	@Override
	public void onPlayerReady(NetPlayer player) {
		/*if(getWorld().isAllPlayersReady() && getWorld().getPlayers().size()>1){
			startGame();
		}*/
	}

}
