package com.entity.network.core.msg;

import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgOnWorldLoaded extends BaseNetMessage {
	public String playerId;
	
	public MsgOnWorldLoaded() {

	}

	public MsgOnWorldLoaded(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	
}
