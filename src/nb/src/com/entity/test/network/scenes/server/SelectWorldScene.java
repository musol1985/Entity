package com.entity.test.network.scenes.server;

import com.entity.core.EntityManager;
import com.entity.core.items.Scene;
import com.entity.test.network.TestNetGameServer;

public class SelectWorldScene extends Scene<TestNetGameServer>{


	public void loadScene() throws Exception {
		EntityManager.getGame().getNet().setMatchName("Mundo 1");
		
		//getApp().showLobby();
	}

}
