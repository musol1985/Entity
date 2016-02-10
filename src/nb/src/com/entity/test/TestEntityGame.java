package com.entity.test;

import com.entity.anot.entities.SceneEntity;
import com.entity.anot.network.Network;
import com.entity.core.EntityGame;
import com.jme3.network.Server;

public class TestEntityGame extends EntityGame{

	
	public static void main(String[] args){
		TestEntityGame test=new TestEntityGame();
		test.simpleInitApp();
	}


}
