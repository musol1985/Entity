package com.entity.network.core.msg;

import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgOnStartGame extends BaseNetMessage {
	public long timestamp;
	public String worldId;

	public MsgOnStartGame(String worldId, long timestamp) {
		this.timestamp = timestamp;
		this.worldId=worldId;
	}
	
	public MsgOnStartGame() {
		
	}
}
