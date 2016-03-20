package com.entity.network.core.msg;

import com.entity.network.core.msg.sync.NetMessage;
import com.jme3.network.AbstractMessage;
import com.jme3.network.Message;
import com.jme3.network.serializing.Serializable;
@Serializable
public class MsgSync<T extends NetMessage> extends AbstractMessage{
	private String id;
	private T field;
	
	public MsgSync(){
		
	}
	
	public MsgSync(String id, T msg){
		this.field=msg;
		this.id=id;
	}
	
	public String getId() {
		return id;
	}
	public T getField() {
		return field;
	}
	
	
}
