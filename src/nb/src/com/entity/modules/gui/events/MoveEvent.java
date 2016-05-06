package com.entity.modules.gui.events;

import com.jme3.math.Vector2f;

public class MoveEvent {
	public Vector2f pos;
	public Vector2f oldPos;
	public float tpf;
	
	public MoveEvent() {

	}

	public void update(Vector2f pos, float tpf){
		if(this.pos!=null)
			this.oldPos=pos;
		
		this.pos=pos;
		this.tpf=tpf;
	}

	public Vector2f getPos() {
		return pos;
	}

	public Vector2f getOldPos() {
		return oldPos;
	}

	public float getTpf() {
		return tpf;
	}
	
	
}
