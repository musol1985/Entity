package com.entity.bean.custom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.components.model.PhysicsBodyComponent;
import com.entity.anot.components.model.collision.CompBoxCollisionShape;
import com.entity.anot.components.model.collision.CompSphereCollisionShape;
import com.entity.anot.components.model.collision.CustomCollisionShape;
import com.entity.anot.modificators.ApplyToComponent;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.math.Vector3f;

public class RigidBodyBean extends AnnotationFieldBean<PhysicsBodyComponent>{
	private Field componentField;
	private Method customShape;
	
	public RigidBodyBean(Class c, Field f)throws Exception{
		super(f, PhysicsBodyComponent.class);

		if(EntityManager.isAnnotationPresent(ApplyToComponent.class,f)){
			componentField=c.getDeclaredField(EntityManager.getAnnotation(ApplyToComponent.class, f).component());
			if(componentField==null)
				throw new Exception("Can't apply RigidBodyControl to field "+EntityManager.getAnnotation(ApplyToComponent.class,f).component()+" in "+c.getName());
			componentField.setAccessible(true);
		}
		if(EntityManager.isAnnotationPresent(CustomCollisionShape.class,f)){
			customShape=c.getDeclaredMethod(EntityManager.getAnnotation(CustomCollisionShape.class,f).methodName(), null);
		}
	}
	
	public CollisionShape getCollisionShape(IEntity entity)throws Exception{
		CollisionShape shape=null;
		
		if(EntityManager.isAnnotationPresent(CompSphereCollisionShape.class,f)){
			shape=new SphereCollisionShape(EntityManager.getAnnotation(CompSphereCollisionShape.class,f).radius());
		}else if(EntityManager.isAnnotationPresent(CompBoxCollisionShape.class,f)){
			CompBoxCollisionShape anot=EntityManager.getAnnotation(CompBoxCollisionShape.class,f);
			
			shape=new BoxCollisionShape(new Vector3f(anot.x(), anot.y(), anot.z()));
			
		}else if(customShape!=null){
			shape=(CollisionShape)customShape.invoke(entity, null);
		}
		
		return shape;
	}

	public Field getComponentField() {
		return componentField;
	}
	
	
}
