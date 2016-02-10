package com.entity.network.core.msg;

import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgOnWorldCreated extends BaseNetMessage {
	public boolean error;

	public MsgOnWorldCreated(boolean error) {
		this.error=error;
	}
	
	public MsgOnWorldCreated() {
		
	}
}
