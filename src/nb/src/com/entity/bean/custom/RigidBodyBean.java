package com.entity.bean.custom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.components.model.PhysicsBodyComponent;
import com.entity.anot.components.model.PhysicsBodyComponent.PhysicsBodyType;
import com.entity.anot.components.model.collision.CompBoxCollisionShape;
import com.entity.anot.components.model.collision.CompSphereCollisionShape;
import com.entity.bean.BodyBean;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;

public class RigidBodyBean extends BodyBean<PhysicsBodyComponent>{
	private Method typeMethod;
	
	public RigidBodyBean(Class c, Field f)throws Exception{
		super(c, f, PhysicsBodyComponent.class);
		
		if(!annot.typeMethod().isEmpty()){
			typeMethod=c.getMethod(annot.typeMethod(), new Class[]{});
			if(typeMethod==null){
				throw new Exception("RigidBodyBean: The method "+annot.typeMethod()+" doesnt exists in "+c.getName());
			}
			
			typeMethod.setAccessible(true);			
		}
	}
	
	private Class getClassType(Object obj)throws Exception{
		if(typeMethod!=null){
			return (Class)typeMethod.invoke(obj, new Object[]{});			
		}
		return Integer.class;
	}
	
	public boolean isGhost(Object obj)throws Exception{
		return getField().getType()==GhostControl.class || getAnnot().type()==PhysicsBodyType.GHOST_BODY || getClassType(obj)==GhostControl.class;
	}
	
	public CollisionShape getCollisionShape(IEntity entity)throws Exception{
		CollisionShape shape=null;
		
		if(EntityManager.isAnnotationPresent(CompSphereCollisionShape.class,f)){
			shape=new SphereCollisionShape(EntityManager.getAnnotation(CompSphereCollisionShape.class,f).radius());
		}else if(EntityManager.isAnnotationPresent(CompBoxCollisionShape.class,f)){
			CompBoxCollisionShape anot=EntityManager.getAnnotation(CompBoxCollisionShape.class,f);
			
			shape=new BoxCollisionShape(new Vector3f(anot.x(), anot.y(), anot.z()));
			
		}else{
			shape=super.getCollisionShape(entity);
		}
		
		return shape;
	}
}
