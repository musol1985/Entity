package com.entity.modules.trails;

import com.jme3.math.Vector3f;

public class TrailPoint {
	protected Vector3f pos;
	protected Vector3f reductionDirection;

	public TrailPoint(Vector3f pos, Vector3f reductionDirection) {
		this.pos = pos;
		this.reductionDirection = reductionDirection;

	}
	
	
}
