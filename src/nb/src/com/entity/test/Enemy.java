package com.entity.test;

import com.entity.anot.components.model.MaterialComponent;
import com.entity.anot.components.model.RigidBodyComponent;
import com.entity.anot.components.model.SubModelComponent;
import com.entity.anot.components.model.collision.CompBoxCollisionShape;
import com.entity.anot.entities.ModelEntity;
import com.entity.anot.modificators.ApplyToComponent;
import com.entity.anot.modificators.ApplyToGeometry;
import com.entity.core.items.Model;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;

@ModelEntity(asset="Models/enemy.j3o")
public class Enemy extends Model{
	
	@RigidBodyComponent
	@CompBoxCollisionShape(x=1, y=3, z=4)
	private PhysicsRigidBody body;
	
	@SubModelComponent(name="leg",rayPickResponse=true)
	@MaterialComponent(asset="Materials/leg.j3m")
	private Geometry leg;
	
	@SubModelComponent(name="head",rayPickResponse=true)
	private Geometry head;
	
	@MaterialComponent(asset="Materials/head.j3m")
	@ApplyToComponent(component="head")
	private Material mat1;
	
	@MaterialComponent(asset="Materials/arm.j3m")
	@ApplyToGeometry(geometry="arm")//this material applies to a geometry in enemy.j3o called arm
	private Material mat2;
	
	@MaterialComponent(asset="Materials/blood.j3m")
	private Material mat3;
}
