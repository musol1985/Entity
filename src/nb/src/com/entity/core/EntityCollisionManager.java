package com.entity.core;

import java.lang.reflect.Method;

import com.entity.core.builders.ModelBuilder;
import com.entity.core.items.Model;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Spatial;

public class EntityCollisionManager implements PhysicsCollisionListener{

	
	
	private static void onCollision(Method m, Model A, Model B, PhysicsCollisionEvent event)throws Exception{
		if(m!=null){
			if(m.getParameterTypes().length==1){
				m.invoke(A, B);
			}else{
				m.invoke(A, B, event);
			}			
		}
	}
	
	private boolean isEntityNode(Spatial node){
		return node!=null && getEntity(node)!=null;
	}

	private Model getEntity(Spatial node){
		return node.getUserData(ModelBuilder.ENTITY_MODEL_REFERENCE);
	}

	public void collision(PhysicsCollisionEvent event) {
		try{
			Model A=null,B=null;
			
			if(isEntityNode(event.getNodeA())){
				A=getEntity(event.getNodeA());
			}
			if(isEntityNode(event.getNodeB())){
				B=getEntity(event.getNodeB());
			}
			
			if(A!=null && B!=null){
				ModelBuilder tA=(ModelBuilder) EntityManager.getBuilder(A.getClass());
				ModelBuilder tB=(ModelBuilder) EntityManager.getBuilder(B.getClass());
				

				onCollision(tA.collidesWith(B), A, B, event);
				onCollision(tB.collidesWith(A), B, A, event);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
