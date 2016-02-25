package com.entity.network.core.dao;

import java.io.Serializable;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.network.MessageConnection;

@com.jme3.network.serializing.Serializable
public class NetPlayerDAO implements Serializable{
	private String id;
	private boolean admin;
	private transient boolean ready;
	private transient MessageConnection cnn;
	private Vector3f position;
	
	public NetPlayerDAO() {
		
	}
	
	public NetPlayerDAO(String id, HostedConnection cnn) {
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

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	
}
