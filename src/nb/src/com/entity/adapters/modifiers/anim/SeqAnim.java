package com.entity.adapters.modifiers.anim;

import com.entity.adapters.ControlAdapter;
import com.entity.adapters.Modifier;
import com.entity.adapters.listeners.IModifierOnFinish;
import com.jme3.scene.Spatial;

public class SeqAnim extends ControlAdapter implements IModifierOnFinish{
    private Modifier[] mods;
    private boolean loop;
    private int current=0;
    
    public SeqAnim( boolean loop, Modifier... mods){
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
        mods[current].update(tpf);
    }

    public void onFinish(Modifier m, Spatial e) {
        if(current<mods.length-1){
            current++;
        }else if(loop){
            for(Modifier mod:mods){
                mod.reset();
            }
            current=0;
        }else{
            e.removeControl(SeqAnim.class);
        }
    }
}
