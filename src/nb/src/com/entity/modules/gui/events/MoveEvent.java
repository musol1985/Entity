package com.entity.modules.gui.events;

import com.jme3.math.Vector2f;

public class MoveEvent {
	public Vector2f pos;
	public Vector2f oldPos;
	public float tpf;
	
	public MoveEvent() {
            pos=new Vector2f(0,0);
            oldPos=new Vector2f(0,0);
	}

	public void update(Vector2f pos, float tpf){
		this.oldPos.x=this.pos.x;
                this.oldPos.y=this.pos.y;
		
		this.pos.set(pos);
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
