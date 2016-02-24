package com.entity.network.core.msg;

import com.entity.core.EntityManager;
import com.jme3.network.AbstractMessage;


public abstract class BaseNetMessage extends AbstractMessage {
	public void send(){
		if(EntityManager.getGame().getNet().isNetClientGame()){
			EntityManager.getGame().getNet().getClient().send(this);
		}else if(EntityManager.getGame().getNet().isNetServerGame()){
			EntityManager.getGame().getNet().getServer().broadcast(this);
		}
	}
	

}
