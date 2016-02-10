package com.entity.test;

import java.util.List;
import java.util.Map;

import com.entity.anot.collections.ListEntity;
import com.entity.anot.collections.ListItemEntity;
import com.entity.anot.collections.MapEntity;
import com.entity.anot.collections.MapEntryEntity;
import com.entity.anot.components.input.KeyInputMapping;
import com.entity.core.items.Model;
import com.entity.core.items.Scene;
import com.jme3.input.KeyInput;

@KeyInputMapping(action="avanzar",keys={KeyInput.KEY_W})
public class Level extends Scene {
	
	//adds 3 players
	@ListEntity(items={
		@ListItemEntity(entityClass=Player.class),
		@ListItemEntity(entityClass=Player.class),
		@ListItemEntity(entityClass=Player.class)
	})
	private List<Player> players;
	
	
	@MapEntity(entries={
			@MapEntryEntity(key="player1", entityClass=Player.class),
			@MapEntryEntity(key="player2", entityClass=Player.class),
	})
	private Map<String, Player> playersMap;
	
	
	@MapEntity(entries={
			@MapEntryEntity(key="player1", entityClass=Player.class),
			@MapEntryEntity(key="Enemy1", entityClass=Enemy.class),
			@MapEntryEntity(key="Enemy2", entityClass=Enemy.class),
	})
	private Map<String, Model> map;
	
	@MapEntity(entries={}, packageItems="com.entity.test.items")
	private Map<String, Model> map2;
	
	public void test(){}

    @Override
    public void loadScene() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

