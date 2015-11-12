package com.entity.test;

import java.awt.event.MouseEvent;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.Entity;
import com.entity.anot.components.input.Input;
import com.entity.anot.components.input.KeyInputMapping;
import com.entity.anot.components.input.MouseButtonInputMapping;
import com.entity.anot.network.MessageListener;
import com.entity.core.items.Scene;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;


public class MainScene extends Scene<TestEntityGame>{
	
	@MessageListener
	private NetworkMessageListener listener;
	
	@Entity
	private Player player;
	


	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		System.out.println(player);
	}
	
	
}
