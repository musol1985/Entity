package com.entity.network.core.msg;

import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgOnNewPlayer extends BaseNetMessage {
	public String nickname;
	public boolean admin;

	public MsgOnNewPlayer(String nickname, boolean admin) {
		this.nickname = nickname;
		this.admin=admin;
	}
	
	public MsgOnNewPlayer() {
		
	}
}
