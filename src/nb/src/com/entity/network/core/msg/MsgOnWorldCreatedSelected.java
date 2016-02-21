package com.entity.network.core.msg;

import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgOnWorldCreatedSelected extends BaseNetMessage {
	public boolean error;
	public boolean created;

	public MsgOnWorldCreatedSelected(boolean error, boolean created) {
		this.error=error;
		this.created=created;
	}
	
	public MsgOnWorldCreatedSelected() {
		
	}
	
	public boolean isCreated(){
		return created;
	}
	
	public boolean isSelected(){
		return !created;
	}
}
