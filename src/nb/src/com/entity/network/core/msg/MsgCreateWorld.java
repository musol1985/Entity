package com.entity.network.core.msg;

import com.entity.network.core.dao.NetWorldDAO;
import com.jme3.network.serializing.Serializable;
@Serializable
public class MsgCreateWorld<T extends NetWorldDAO> extends BaseNetMessage {
	public T world;	

	public MsgCreateWorld(T world) {
		this.world=world;
	}
	
	public MsgCreateWorld() {
		
	}
}
