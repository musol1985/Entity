package com.entity.network.core.msg;

import java.util.List;

import com.entity.network.core.bean.NetWorld;
import com.jme3.network.serializing.Serializable;
@Serializable
public class MsgCreateWorld extends BaseNetMessage {
	public NetWorld world;	

	public MsgCreateWorld(NetWorld world) {
		this.world=world;
	}
	
	public MsgCreateWorld() {
		
	}
}
