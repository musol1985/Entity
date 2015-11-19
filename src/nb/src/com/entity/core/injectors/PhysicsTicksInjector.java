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

import com.entity.anot.PrePhysicsTick;
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
public class PhysicsTicksInjector<T extends IEntity> implements Injector<T>, InjectorAttachable<T>,Comparable<PhysicsTicksInjector>{
    private List<Method> pre=new ArrayList<Method>();
	
	
    @Override
    public int compareTo(PhysicsTicksInjector t) {
        return -1;
    }

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		
	}
		

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {
		if(m.isAnnotationPresent(PrePhysicsTick.class)){
			m.setAccessible(true);
			pre.add(m);
		}
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasInjections() {
		return pre.size()>0;
	}

	@Override
	public void onInstance(T item, IBuilder builder) throws Exception {

	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance)throws Exception {

	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance)throws Exception {

	}
	
	
}
