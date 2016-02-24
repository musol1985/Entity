package com.entity.network.core.msg;

import com.entity.network.core.bean.NetWorld;
import com.jme3.network.serializing.Serializable;
@Serializable
public class MsgCreateWorld<T extends NetWorld> extends BaseNetMessage {
	public T world;	

	public MsgCreateWorld(T world) {
		this.world=world;
	}
	
	public MsgCreateWorld() {
		
	}
}
