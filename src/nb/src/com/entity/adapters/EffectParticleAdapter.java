/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.adapters;

import java.util.ArrayList;
import java.util.List;
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
	
	public EffectParticleAdapter(String asset){
		this.asset=asset;
	}
	
	public void attach(T parent, Vector3f position){
		ParticleEmitter pe=null;
		Stack<ParticleEmitter> stack=EntityManager.getGame().getParticlesCache().get(asset);
		if(stack!=null){
            if(stack.size()>0)
            	pe=(ParticleEmitter)stack.pop();
		}else{
			stack=new Stack<ParticleEmitter>();
			EntityManager.getGame().getParticlesCache().put(asset, stack);
		}
		
		if(pe==null){
			pe=(ParticleEmitter)((Node)EntityManager.getAssetManager().loadModel(asset)).getChild(0);
			pe.setUserData(EMITER_INITIAL_PARTICLES, pe.getParticlesPerSec());
		}else{
			pe.setParticlesPerSec((Float)pe.getUserData(EMITER_INITIAL_PARTICLES));				
		}
		
		parent.getNode().attachChild(pe);
		
		pe.setLocalTranslation(position);
		
		pe.emitAllParticles();
		pe.setParticlesPerSec(0);
		pe.addControl(new ParticleDettachListener(pe){
			@Override
			public void onDettach(ParticleEmitter emiter) {
				EntityManager.getGame().getParticlesCache().get(asset).push(emiter);
			}
		});
	}
        
	
}

