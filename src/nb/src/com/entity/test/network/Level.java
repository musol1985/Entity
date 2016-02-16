package com.entity.test.network;

import com.entity.anot.Entity;
import com.entity.anot.components.input.KeyInputMapping;
import com.entity.core.items.Scene;
import com.jme3.input.KeyInput;

@KeyInputMapping(action="avanzar",keys={KeyInput.KEY_W})
public class Level extends Scene {
	
	@Entity(name="player1")
	private TestNetCharacter player;
	
	@Entity(name="enemy1")
	private TestNetCharacter e1;
	@Entity(name="enemy2")
	private TestNetCharacter e2;
	

	public void loadScene() {
		//sets player and enemies name ID
		player.setName("Mike");
		
		e1.setName("July");
		e2.setName("Ron");
		
		
		player.netControl();
	}
}

