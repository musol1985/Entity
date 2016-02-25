package com.entity.network.core.msg;

import java.util.List;

import com.entity.network.core.dao.NetWorldDAO;
import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgSelectWorld extends BaseNetMessage {
	public String world;	

	public MsgSelectWorld(String world) {
		this.world=world;
	}
	
	public MsgSelectWorld() {
		
	}
}
