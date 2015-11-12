package com.entity.test;

public class SphereCollisionShape implements CollisionShape{
	float radius;
	
	public SphereCollisionShape(float radius){
		this.radius=radius;
	}

	public String test() {
		return "Spehre"+radius;
	}
	


}
