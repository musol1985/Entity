package com.entity.network.core.msg;

import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgOnStartGame extends BaseNetMessage {
	public long timestamp;

	public MsgOnStartGame(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public MsgOnStartGame() {
		
	}
}
