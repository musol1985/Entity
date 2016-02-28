package com.entity.test.network;

import com.entity.anot.entities.SceneEntity;
import com.entity.anot.network.Network;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.network.core.service.NetWorldService;
import com.entity.test.network.scenes.server.LobbyScene;
import com.entity.test.network.scenes.server.SelectWorldScene;

@Network(messagesPackage={"com.entity.test.msg"}, gameName="Test", version=1, worldService = NetWorldService.class)
public class TestNetGameServer extends EntityGame{
	
	public static void main(String[] args)throws Exception{
		EntityManager.startGame(TestNetGameServer.class);
	}
/*
	@SceneEntity(first=true)
	public SelectWorldScene showSelectWorld(){
		return new SelectWorldScene();
	}
	
	@SceneEntity(preLoad=false, singleton=false)
	public LobbyScene showLobby(){
		return new LobbyScene();
	}

	@SceneEntity(preLoad=false, singleton=false)
	public LobbyScene showGame(){
		return new LobbyScene();
	}*/
}
