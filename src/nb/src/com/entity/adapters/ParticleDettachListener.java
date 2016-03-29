/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.adapters;


/**
 *
 * @author Edu
 */

public class ParticleDettachListener extends ControlAdapter{
	private Particle p;
	
	public ParticleDettachListener(Particle p){
		this.p=p;
		p.addControl(this);
	}
	
	@Override
    public void update(float tpf) {        
        if(emiter.getNumVisibleParticles()==0){
            emiter.getParent().detachChild(emiter);
        }
    }
}

