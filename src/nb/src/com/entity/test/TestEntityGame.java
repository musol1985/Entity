package com.entity.test;

import com.entity.anot.entities.SceneEntity;
import com.entity.anot.network.Network;
import com.entity.core.EntityGame;
import com.jme3.network.Server;

public class TestEntityGame extends EntityGame{
	@Network(messagesPackage="com.entity.test.msg")
	private Server server;
	
	@SceneEntity(attach=true, name="main")
	private MainScene mainScene;
	
	
	public static void main(String[] args){
		TestEntityGame test=new TestEntityGame();
		test.simpleInitApp();
	}

	@Override
	public void simpleInitApp() {
		super.simpleInitApp();
		
		mainScene.initialize(getStateManager(), this);
	}
	
	

}
