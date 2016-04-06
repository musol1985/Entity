package com.entity.adapters.bean;

public class ModifierValueBean {
    private float v;    
    private float value;
    private float delta;
    private float dist;
    private float from;
    
    
    public ModifierValueBean(float from, float to, float time){
    	this.from=from;
    	dist=to-from;
        v=dist/(time/1000);
        if(!hasMod()){
        	value=from;
        	delta=0;
        }
    }
    
    public void update(float time){
    	if(hasMod()){
	    	delta=v*time;
	    	
	    	if(Math.abs(delta)>Math.abs(dist)){
                        delta=dist;
	    		value=from+dist;
                }else{
                    value=from+delta;
                }
	    	
	    	
    	}
    }
    
    public boolean isFinished(){
    	return value==from+dist;
    }

	public float getValue() {
		return value;
	}

	public float getDelta() {
		return delta;
	}
    
    public boolean hasMod(){
    	return dist!=0;
    }
}
