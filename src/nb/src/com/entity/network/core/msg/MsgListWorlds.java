package com.entity.network.core.msg;

import java.util.HashMap;

import com.entity.network.core.bean.NetWorld;
import com.jme3.network.serializing.Serializable;
@Serializable
public class MsgListWorlds<T extends NetWorld> extends BaseNetMessage {
	public HashMap<String,T> worlds;	

	public MsgListWorlds(HashMap<String, T> worlds) {
		this.worlds=worlds;
	}
	
	public MsgListWorlds() {
		
	}
}
