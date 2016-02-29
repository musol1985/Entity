package com.entity.network.core.dao;

import java.io.Serializable;

import com.entity.network.core.beans.CellId;

@com.jme3.network.serializing.Serializable
public class NetWorldCellDAO implements Serializable{
	private CellId id;

	public CellId getId() {
		return id;
	}

	public void setId(CellId id) {
		this.id = id;
	}

	
}
