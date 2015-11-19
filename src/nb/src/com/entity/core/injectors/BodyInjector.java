/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.components.model.PhysicsBodyComponent;
import com.entity.anot.components.model.PhysicsBodyComponent.PhysicsBodyType;
import com.entity.bean.RigidBodyBean;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.entity.core.InjectorAttachable;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;


/**
 *
 * @author Edu
 */
public class BodyInjector<T extends IEntity> implements Injector<T>, InjectorAttachable<T>,Comparable<BodyInjector>{
    private List<RigidBodyBean> bodies=new ArrayList<RigidBodyBean>();
	
	
    @Override
    public int compareTo(BodyInjector t) {
        return -1;
    }

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(PhysicsBodyComponent.class)){
			f.setAccessible(true);
			bodies.add(new RigidBodyBean(c, f));
		}
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasInjections() {
		return bodies.size()>0;
	}

	@Override
	public void onInstance(T item, IBuilder builder) throws Exception {
		for(RigidBodyBean bean:bodies){

			PhysicsControl  body=null;
			if(bean.getAnot().type()==PhysicsBodyType.RIGID_BODY){
				body=new RigidBodyControl(bean.getCollisionShape(), bean.getAnot().mass());
			}else if(bean.getAnot().type()==PhysicsBodyType.KINEMATIC_BODY){
				body=new RigidBodyControl(bean.getCollisionShape(), bean.getAnot().mass());
				((RigidBodyControl)body).setKinematic(true);
			}else{
				body=new GhostControl(bean.getCollisionShape());
			}
			bean.getF().set(item, body);			
		}
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance)throws Exception {
		for(RigidBodyBean bean:bodies){
			if(bean.getAnot().attachWorld()){
				PhysicsControl  body=(PhysicsControl)bean.getF().get(instance);	
				if(bean.getComponentField()!=null){
					IEntity node=(IEntity) bean.getComponentField().get(instance);
					node.getNode().addControl(body);
				}
				EntityManager.getCurrentScene().getPhysics().add(body);
			}
		}
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance)throws Exception {
		for(RigidBodyBean bean:bodies){
			if(bean.getAnot().attachWorld()){
				PhysicsControl body=(PhysicsControl)bean.getF().get(instance);	
				if(bean.getComponentField()!=null){
					IEntity node=(IEntity) bean.getComponentField().get(instance);
					node.getNode().removeControl(body);
				}
				EntityManager.getCurrentScene().getPhysics().remove(body);
			}
		}
	}
	
	
}
