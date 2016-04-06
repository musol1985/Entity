package com.entity.adapters;

import com.entity.adapters.bean.ModifierValueBean;
import com.entity.adapters.listeners.IModifierOnFinish;
import com.entity.core.items.ModelBase;
import com.jme3.scene.Spatial;

public abstract class Modifier<S extends Spatial> extends ControlAdapter {
	protected ModifierValueBean[] values;
	protected S s;
	protected float time;
	protected boolean loop;
	protected IModifierOnFinish finishListener;
	
	public Modifier(){
		
	}
	
	public Modifier(float time, boolean loop, ModifierValueBean... values){
		this.time=0;
		this.loop=loop;
		this.values=values;
	}
	


	public void setFinishListener(IModifierOnFinish finishListener) {
		this.finishListener = finishListener;
	}


	@Override
	public void setSpatial(Spatial s) {
		this.s=(S)s;
	}

	@Override
	public void update(float tpf) {
		time+=tpf;
		
		boolean finish=true;
		
		for(ModifierValueBean value:values){
			value.update(time);
			if(!value.isFinished())
				finish=false;
		}
		
		onUpdate(tpf, values);
		
		if(finish){
			if(!loop){
				S te=s;
                s.removeControl(this);
                if(finishListener!=null)
                	finishListener.onFinish(this, te);
			}else{
				time=0;
			}
		}
	}
	
	public void reset(){
        time=0;
    }

	public ModifierValueBean getValue(int index){
		return values[index];
	}
	
	public abstract void onUpdate(float tpf, ModifierValueBean[] values);
}
