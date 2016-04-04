/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.adapters;

import java.util.Stack;

import com.entity.core.EntityManager;
import com.entity.core.items.ModelBase;
import com.jme3.effect.ParticleEmitter;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


/**
 *
 * @author Edu
 */
public class ParticleCache<T extends ModelBase>{	
	public static final String EMITER_INITIAL_PARTICLES="numParticles";
	
	private ParticleEmitter emiter;
	private String asset;
	
	public ParticleCache(String asset){
		this.asset=asset;
	}
	
	public void attach(T parent){
		if(emiter==null){
			Stack<ParticleEmitter> stack=EntityManager.getGame().getParticlesCache().get(asset);
			if(stack!=null){
                            if(stack.size()>0)
				emiter=(ParticleEmitter)stack.pop();
			}else{
				stack=new Stack<ParticleEmitter>();
				EntityManager.getGame().getParticlesCache().put(asset, stack);
			}
			
			if(emiter==null){
				emiter=(ParticleEmitter)((Node)EntityManager.getAssetManager().loadModel(asset)).getChild(0);
				emiter.setUserData(EMITER_INITIAL_PARTICLES, emiter.getParticlesPerSec());
			}else{
				emiter.setParticlesPerSec((Float)emiter.getUserData(EMITER_INITIAL_PARTICLES));				
			}
		}
		onAttached(parent, emiter);
		parent.getNode().attachChild(emiter);
	}
        
	
	public void onAttached(T parent, ParticleEmitter emiter){
		
	}
        
        public void setLocalTranslation(Vector3f pos){
            emiter.setLocalTranslation(pos);
        }
	
	public void dettach(boolean emitAll){
		if(emitAll)
			emiter.emitAllParticles();
		
		emiter.setParticlesPerSec(0);	
		emiter.addControl(new ParticleDettachListener());
	}
	
	class ParticleDettachListener extends ControlAdapter{
		@Override
	    public void update(float tpf) {        
	        if(emiter.getNumVisibleParticles()==0){
	        	if(emiter.getParent()!=null)
	        		emiter.getParent().detachChild(emiter);
	            emiter.removeControl(this);
	            EntityManager.getGame().getParticlesCache().get(asset).push(emiter);
	            emiter=null;
	        }
	    }
	}

        public void emitAll(){
            emiter.emitAllParticles();
        }
}

