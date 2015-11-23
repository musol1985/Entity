package com.entity.network;

import com.jme3.network.Message;

public class SyncMessage<T> implements Message{
	private String id;
	private T field;
	
	public SyncMessage(String id, T msg){
		this.field=msg;
		this.id=id;
	}
	
	public String getId() {
		return id;
	}
	public T getField() {
		return field;
	}

	@Override
	public boolean isReliable() {
		return true;
	}
	@Override
	public Message setReliable(boolean arg0) {
		return null;
	} 
	
	
}
