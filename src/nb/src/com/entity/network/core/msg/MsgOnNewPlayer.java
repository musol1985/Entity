package com.entity.network.core.msg;

import com.entity.network.core.dao.NetPlayerDAO;
import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgOnNewPlayer extends BaseNetMessage {
	public NetPlayerDAO player;

	public MsgOnNewPlayer(NetPlayerDAO player) {
		this.player=player;
	}
	
	public MsgOnNewPlayer() {
		
	}
}
