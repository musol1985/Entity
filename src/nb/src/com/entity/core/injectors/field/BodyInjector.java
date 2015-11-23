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
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;


/**
 *
 * @author Edu
 */
public class BodyInjector<T extends IEntity> extends ListBeanInjector<RigidBodyBean, T> implements InjectorAttachable<T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(PhysicsBodyComponent.class)){
			beans.add(new RigidBodyBean(c, f));
		}
	}

	@Override
	public void onInstance(T item, IBuilder builder) throws Exception {
		for(RigidBodyBean bean:beans){
			
			PhysicsControl  body=null;
			if(bean.getAnnot().type()==PhysicsBodyType.RIGID_BODY){
				body=new RigidBodyControl(bean.getCollisionShape(item), bean.getAnnot().mass());
			}else if(bean.getAnnot().type()==PhysicsBodyType.KINEMATIC_BODY){
				body=new RigidBodyControl(bean.getCollisionShape(item), bean.getAnnot().mass());
				((RigidBodyControl)body).setKinematic(true);
			}else{
				body=new GhostControl(bean.getCollisionShape(item));
			}
			bean.getField().set(item, body);			
		}
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance)throws Exception {
		for(RigidBodyBean bean:beans){
			if(bean.getAnnot().attachWorld()){
				PhysicsControl  body=(PhysicsControl)bean.getField().get(instance);	
				if(bean.getComponentField()!=null){
					IEntity node=(IEntity) bean.getComponentField().get(instance);
					node.getNode().addControl(body);
				}else{
					instance.getNode().addControl(body);
				}
				EntityManager.getCurrentScene().getPhysics().add(body);
			}
		}
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance)throws Exception {
		for(RigidBodyBean bean:beans){
			if(bean.getAnnot().attachWorld()){
				PhysicsControl body=(PhysicsControl)bean.getField().get(instance);	
				if(bean.getComponentField()!=null){
					IEntity node=(IEntity) bean.getComponentField().get(instance);
					node.getNode().removeControl(body);
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
