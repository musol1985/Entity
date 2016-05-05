/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.components.model.PhysicsBodyComponent;
import com.entity.anot.components.model.PhysicsBodyComponent.PhysicsBodyType;
import com.entity.bean.custom.RigidBodyBean;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.BaseInjector;
import com.entity.core.injectors.ListBeanInjector;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;


/**
 *
 * @author Edu
 */
public class BodyInjector<T extends IEntity> extends ListBeanInjector<RigidBodyBean, T> implements InjectorAttachable<T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {            
		if(EntityManager.isAnnotationPresent(PhysicsBodyComponent.class,f)){
			beans.add(new RigidBodyBean(c, f));
		}
	}

	@Override
	public void onInstance(T item, IBuilder builder, Object[] params) throws Exception {
		for(RigidBodyBean bean:beans){
			
			PhysicsControl  body=null;
			if(!bean.getAnnot().nodeName().isEmpty()){
				Spatial node=item.getNode().getChild(bean.getAnnot().nodeName());
				if(node!=null){
					body=node.getControl(PhysicsControl.class);
				}else{
					log.warning("@Body.onInstance: The node "+bean.getAnnot().nodeName()+" doesn't exists in class "+item.getClass().getName());
				}
			}else if(bean.getAnnot().type()==PhysicsBodyType.RIGID_BODY){
				body=new RigidBodyControl(bean.getCollisionShape(item), bean.getAnnot().mass());				
			}else if(bean.getAnnot().type()==PhysicsBodyType.KINEMATIC_BODY){
				body=new RigidBodyControl(bean.getCollisionShape(item), bean.getAnnot().mass());
				((RigidBodyControl)body).setKinematic(true);
			}else{
				CollisionShape shape=bean.getCollisionShape(item);
				if(shape!=null){
					body=new GhostControl(shape);
				}else{
					body=new GhostControl();
				}
			}
			bean.getField().set(item, body);			
		}
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance)throws Exception {
		//if(EntityManager.getGame().isPhysics())
			for(RigidBodyBean bean:beans){
				if(bean.getAnnot().attachWorld()){
					PhysicsControl  body=(PhysicsControl)bean.getField().get(instance);	
					if(bean.getComponentField()!=null){
						Object component=bean.getComponentField().get(instance);
						if(component instanceof IEntity){
							((IEntity)component).getNode().addControl(body);
						}else{
							((Spatial)component).addControl(body);
						}
					}else{
						instance.getNode().addControl(body);
					}                                
					EntityManager.getCurrentScene().getPhysics().add(body);
				}
			}
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance)throws Exception {
            if(EntityManager.getGame().isPhysics())
		for(RigidBodyBean bean:beans){
			if(bean.getAnnot().attachWorld()){
				PhysicsControl body=(PhysicsControl)bean.getField().get(instance);	
				if(bean.getComponentField()!=null){
                                        Object component=bean.getComponentField().get(instance);
					if(component instanceof IEntity){
						((IEntity)component).getNode().removeControl(body);
					}else{
						((Spatial)component).removeControl(body);
					}
				}else{
					instance.getNode().removeControl(body);
				}
				EntityManager.getCurrentScene().getPhysics().remove(body);
			}
		}
	}
	
	@Override
    public int compareTo(BaseInjector t) {
        return 1;
    }
}
