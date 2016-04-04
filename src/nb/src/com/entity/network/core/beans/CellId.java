package com.entity.network.core.beans;

import java.io.Serializable;

import com.entity.utils.Vector2;

@com.jme3.network.serializing.Serializable
public class CellId implements Serializable{
	public Vector2 id;
	public long timestamp;
	
	
	public CellId() {

	}
	public CellId(Vector2 id, long timestamp) {
		this.id = id;
		this.timestamp = timestamp;
	}
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

	public boolean equals(Object obj) {
		if(obj instanceof CellId){
			return (id.equals(((CellId) obj).getId()));
		}
		return false;
	}
		
        public String toString(){
            return "CellId: "+id.toString()+" - "+timestamp;
        }
        
        public void update(){
            timestamp=System.currentTimeMillis();
        }
}
