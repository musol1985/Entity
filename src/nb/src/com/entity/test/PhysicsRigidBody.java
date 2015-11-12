package com.entity.test;

public class PhysicsRigidBody {
	float mass;
	CollisionShape shape;
	public PhysicsRigidBody(float mass){
		this.mass=mass;
	}
	public PhysicsRigidBody(float mass, CollisionShape shape){
		this.mass=mass;
		this.shape=shape;
	}
	
	
	public void test(){
		System.out.println("Rigid body "+mass);
		System.out.println("Rigid body shape "+shape.test());
	}
}
