package com.entity.network.core.msg;

import java.util.HashMap;

import com.entity.network.core.bean.NetWorld;
import com.jme3.network.serializing.Serializable;
@Serializable
public class MsgListWorlds extends BaseNetMessage {
	public HashMap<String,NetWorld> worlds;	

	public MsgListWorlds(HashMap<String, NetWorld> worlds) {
		this.worlds=worlds;
	}
	
	public MsgListWorlds() {
		
	}
}
