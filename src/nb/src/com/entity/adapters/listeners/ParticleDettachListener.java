package com.entity.adapters.listeners;

import com.entity.adapters.ControlAdapter;
import com.jme3.effect.ParticleEmitter;

public abstract class ParticleDettachListener extends ControlAdapter{
	protected ParticleEmitter emiter;
	
	public ParticleDettachListener(ParticleEmitter pe) {
		this.emiter=pe;
	}
	
	@Override
    public void update(float tpf) {        
        if(emiter!=null && emiter.getNumVisibleParticles()==0){
        	emiter.getParent().detachChild(emiter);
            emiter.removeControl(this);
            onDettach(emiter);
            emiter=null;
        }
    }
	
	public abstract void onDettach(ParticleEmitter pe);
}
