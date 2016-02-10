package com.entity.network.core.msg;

import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgOnPlayerReady extends BaseNetMessage {
	public String nickname;

	public MsgOnPlayerReady(String nickname) {
		this.nickname = nickname;
	}
	
	public MsgOnPlayerReady() {
		
	}
}
