package com.entity.test;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.Entity;
import com.entity.anot.network.MessageListener;
import com.entity.core.items.Scene;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;


public class MainScene extends Scene<TestEntityGame>{
	
	@MessageListener(ignoreSyncMessages=true)
	private NetworkMessageListener listener;
	
	@Entity
	private Player player;
	


	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		System.out.println(player);
		
		player.netControl();
		player.netUnControl();
	}
	
	
}
