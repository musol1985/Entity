/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.adapters;

import java.util.Stack;

import com.entity.adapters.listeners.ParticleDettachListener;
import com.entity.core.EntityManager;
import com.entity.core.items.ModelBase;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


/**
 *
 * @author Edu
 */
public class EffectParticleAdapter<T extends ModelBase>{	
	public static final String EMITER_INITIAL_PARTICLES="numParticles";
	
	private String asset;
	private ParticleEmitter currentPE;
	
	public EffectParticleAdapter(String asset){
		this.asset=asset;
	}
	
	public void attach(T parent, Vector3f position){
		attach(parent, position, false);
	}
	
	public void attach(T parent, Vector3f position, boolean loop){
		Stack<ParticleEmitter> stack=EntityManager.getGame().getParticlesCache().get(asset);
		if(stack!=null){
            if(stack.size()>0)
            	currentPE=(ParticleEmitter)stack.pop();
		}else{
			stack=new Stack<ParticleEmitter>();
			EntityManager.getGame().getParticlesCache().put(asset, stack);
		}
		
		if(currentPE==null){
			currentPE=(ParticleEmitter)((Node)EntityManager.getAssetManager().loadModel(asset)).getChild(0);
			currentPE.setUserData(EMITER_INITIAL_PARTICLES, currentPE.getParticlesPerSec());
		}else{
			currentPE.setParticlesPerSec((Float)currentPE.getUserData(EMITER_INITIAL_PARTICLES));				
		}
		
		parent.getNode().attachChild(currentPE);
		
		currentPE.setLocalTranslation(position);
		
		if(!loop){
			currentPE.emitAllParticles();
			currentPE.setParticlesPerSec(0);
			currentPE.addControl(new ParticleDettachListener(currentPE){
				@Override
				public void onDettach(ParticleEmitter emiter) {
					EntityManager.getGame().getParticlesCache().get(asset).push(emiter);
				}
			});
		}
	}
	
	public void dettach(){
		if(currentPE!=null){
			EntityManager.getGame().getParticlesCache().get(asset).push(currentPE);
			currentPE=null;
		}
	}
        
	
}

