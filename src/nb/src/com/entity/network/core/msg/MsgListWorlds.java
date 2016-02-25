package com.entity.network.core.msg;

import java.util.HashMap;

import com.entity.network.core.dao.NetWorldDAO;
import com.jme3.network.serializing.Serializable;
@Serializable
public class MsgListWorlds<T extends NetWorldDAO> extends BaseNetMessage {
	public HashMap<String,T> worlds;	

	public MsgListWorlds(HashMap<String, T> worlds) {
		this.worlds=worlds;
	}
	
	public MsgListWorlds() {
		
	}
}
