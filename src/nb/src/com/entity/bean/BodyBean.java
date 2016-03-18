package com.entity.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.components.model.collision.CustomCollisionShape;
import com.entity.anot.modificators.ApplyToComponent;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.terrain.geomipmap.TerrainQuad;

public class BodyBean<T extends Annotation> extends AnnotationFieldBean<T>{
	protected Field componentField;
	protected Method customShape;
	protected CollisionShape singletonShape;
	protected boolean singleton;
	
	public BodyBean(Class c, Field f, Class<T> anot)throws Exception{
		super(f, anot);

		if(EntityManager.isAnnotationPresent(ApplyToComponent.class,f)){
			componentField=c.getDeclaredField(EntityManager.getAnnotation(ApplyToComponent.class, f).component());
			if(componentField==null)
				throw new Exception("Can't apply Body to field "+EntityManager.getAnnotation(ApplyToComponent.class,f).component()+" in "+c.getName());
			componentField.setAccessible(true);
		}
		CustomCollisionShape customShape=EntityManager.getAnnotation(CustomCollisionShape.class,f);
		if(customShape!=null){
			singleton=customShape.singleton();
			this.customShape=c.getDeclaredMethod(EntityManager.getAnnotation(CustomCollisionShape.class,f).methodName(), null);			
		}
	}
	
	public CollisionShape getCollisionShape(IEntity entity)throws Exception{
		CollisionShape shape=null;
		
		if(singletonShape!=null)
			return singletonShape;
		
		if(customShape!=null){
			shape=(CollisionShape)customShape.invoke(entity, null);
			if(singleton)
				singletonShape=shape;
		}else if(componentField!=null){
			Object fieldValue=componentField.get(entity);
			if(fieldValue!=null && fieldValue instanceof TerrainQuad){
				shape=CollisionShapeFactory.createMeshShape((TerrainQuad)fieldValue);
			}			
		}
		
		return shape;
	}

	public Field getComponentField() {
		return componentField;
	}
	
	
}
