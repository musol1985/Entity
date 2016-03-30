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
	    	value=v*time;
	    	
	    	if(Math.abs(value)>Math.abs(dist))
	    		value=dist;
	    	
	    	delta=from+value;
    	}
    }
    
    public boolean isFinished(){
    	return value==dist;
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
