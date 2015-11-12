package com.entity.test;

public class BoxCollisionShape implements CollisionShape{
	float x, y,z;
	public BoxCollisionShape(float x, float y, float z){
		this.x=x;
		this.z=z;
		this.y=y;
	}
	public String test() {
		return "Box"+x+","+y+","+z;
	}
	


}
