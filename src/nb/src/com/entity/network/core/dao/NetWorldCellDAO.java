package com.entity.network.core.dao;

import java.io.Serializable;

import com.entity.utils.Vector2;

@com.jme3.network.serializing.Serializable
public class NetWorldCellDAO implements Serializable{
	private Vector2 id;
	private long timestamp;

	public Vector2 getId() {
		return id;
	}

	public void setId(Vector2 id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
