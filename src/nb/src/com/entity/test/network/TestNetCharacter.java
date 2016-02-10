package com.entity.test.network;

import com.entity.anot.entities.ModelEntity;
import com.entity.anot.network.NetSync;
import com.entity.core.items.NetworkModel;

@ModelEntity(asset="Models/character.j3o", name="character")
public class TestNetCharacter extends NetworkModel{
	@NetSync(timeout=10)
	private NetPosition position;


}
