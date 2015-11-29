package com.entity.bean.custom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.components.model.PhysicsBodyComponent;
import com.entity.anot.components.model.collision.CompBoxCollisionShape;
import com.entity.anot.components.model.collision.CompSphereCollisionShape;
import com.entity.anot.components.model.collision.CustomCollisionShape;
import com.entity.anot.modificators.ApplyToComponent;
import com.entity.bean.AnnotationFieldBean;
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

		if(f.isAnnotationPresent(ApplyToComponent.class)){
			componentField=c.getDeclaredField(f.getAnnotation(ApplyToComponent.class).component());
			if(componentField==null)
				throw new Exception("Can't apply RigidBodyControl to field "+f.getAnnotation(ApplyToComponent.class).component()+" in "+c.getName());
			componentField.setAccessible(true);
		}
		if(f.isAnnotationPresent(CustomCollisionShape.class)){
			customShape=c.getDeclaredMethod(f.getAnnotation(CustomCollisionShape.class).methodName(), null);
		}
	}
	
	public CollisionShape getCollisionShape(IEntity entity)throws Exception{
		CollisionShape shape=null;
		
		if(f.isAnnotationPresent(CompSphereCollisionShape.class)){
			shape=new SphereCollisionShape(f.getAnnotation(CompSphereCollisionShape.class).radius());
		}else if(f.isAnnotationPresent(CompBoxCollisionShape.class)){
			CompBoxCollisionShape anot=f.getAnnotation(CompBoxCollisionShape.class);
			
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