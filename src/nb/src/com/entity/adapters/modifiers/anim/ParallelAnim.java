package com.entity.adapters.modifiers.anim;

import com.entity.adapters.ControlAdapter;
import com.entity.adapters.Modifier;
import com.entity.adapters.listeners.IModifierOnFinish;
import com.jme3.scene.Spatial;

public class ParallelAnim extends ControlAdapter implements IModifierOnFinish{
    private Modifier[] mods;
    private boolean loop;
    private int finished;
    
    public ParallelAnim( boolean loop, Modifier... mods){
        this.mods=mods;
        this.loop=loop;             
    }

    @Override
    public void setSpatial(Spatial spatial) {
        for(Modifier m:mods){
            m.setFinishListener(this);
            m.setSpatial(spatial);
        }
    }

    public void update(float tpf) {
    	for(Modifier m:mods){
            m.update(tpf);
        }
    }

    public void onFinish(Modifier m, Spatial e) {
        finished++;
    	if(finished>=mods.length){
	    	if(loop){
	            for(Modifier mod:mods){
	                mod.reset();
	            }
	        }else{
	            e.removeControl(ParallelAnim.class);
	        }
    	}
    }
}
