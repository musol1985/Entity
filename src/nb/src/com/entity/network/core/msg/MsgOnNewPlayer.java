package com.entity.network.core.msg;

import com.entity.network.core.bean.NetPlayer;
import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgOnNewPlayer extends BaseNetMessage {
	public NetPlayer player;

	public MsgOnNewPlayer(NetPlayer player) {
		this.player=player;
	}
	
	public MsgOnNewPlayer() {
		
	}
}
