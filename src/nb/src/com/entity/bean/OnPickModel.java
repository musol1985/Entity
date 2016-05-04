package com.entity.bean;

import com.entity.core.items.ModelBase;
import com.jme3.collision.CollisionResult;

public class OnPickModel{
	private ModelBase m;
	private CollisionResult collision;
	
	public OnPickModel(ModelBase m, CollisionResult c){
		this.m=m;
		this.collision=c;
	}

	public ModelBase getM() {
		return m;
	}

	public void setM(ModelBase m) {
		this.m = m;
	}

	public CollisionResult getCollision() {
		return collision;
	}

	public void setCollision(CollisionResult collision) {
		this.collision = collision;
	}
	
	
}
