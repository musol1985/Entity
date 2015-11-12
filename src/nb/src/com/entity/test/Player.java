package com.entity.test;

import com.entity.anot.OnCollision;
import com.entity.anot.OnUpdate;
import com.entity.anot.RayPick;
import com.entity.anot.RunGLThread;
import com.entity.anot.components.model.RigidBodyComponent;
import com.entity.anot.components.model.SubModelComponent;
import com.entity.anot.components.model.collision.CompBoxCollisionShape;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.items.Model;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

@ModelEntity(asset="Models/player.j3o", name="Girl")
public class Player extends Model{
	
	@RigidBodyComponent
	@CompBoxCollisionShape(x=1, y=3, z=4)
	private PhysicsRigidBody body;
	
	@SubModelComponent(name="Node2")
	private Node node2;
	
	@SubModelComponent(name="geom1")
	private Geometry geo;

	@RunGLThread
	public void onTest(){
		System.out.println("onTest");
		body.test();
		System.out.println(node2);
		System.out.println(geo);
	}
	/*
	@Input(action="avanzar")
	public void avanzar(boolean pressed, float tpf){
		rayEnemy(new Ray(), null, null);
		System.out.println(rayEnemyReturn(new Ray(), null));
	}*/
	
	@OnUpdate
	public void update(float tpf){
		
	}
	
	@OnCollision
	public void onHitEnemy(Enemy enemy, PhysicsCollisionEvent event){
		
	}

	@RayPick
	public void rayEnemy(Ray ray, Enemy enemy, CollisionResult res){
		
	}
	
	@RayPick
	public void ray1(Ray ray, Enemy enemy){
		
	}
	
	@RayPick
	public boolean rayEnemyReturn(Ray ray, Enemy enemy){
		return true;
	}
	
	@RayPick(EntityFilter={Enemy.class, Player.class})
	public void rayEnemy(Ray ray, CollisionResults results){
		
	}
}
