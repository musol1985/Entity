package com.entity.network.core.bean;

import java.io.Serializable;

import com.entity.network.core.msg.MsgOnNewPlayer;
import com.jme3.network.HostedConnection;
import com.jme3.network.MessageConnection;

@com.jme3.network.serializing.Serializable
public class NetPlayer<M extends MsgOnNewPlayer> implements Serializable{
	private String id;
	private boolean admin;
	private transient boolean ready;
	private transient MessageConnection cnn;
	
	public NetPlayer() {
		
	}
	
	public NetPlayer(String id, HostedConnection cnn) {
		this.id = id;
		this.cnn = cnn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MessageConnection getCnn() {
		return cnn;
	}
	public void setCnn(MessageConnection cnn) {
		this.cnn = cnn;
	}
	
	
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public boolean isConnected(){
		return cnn!=null;
	}

	public boolean isReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	
}
