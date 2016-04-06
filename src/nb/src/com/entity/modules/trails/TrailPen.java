package com.entity.modules.trails;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;

public class TrailPen {
	protected List<TrailPoint> points=new ArrayList<TrailPoint>();
	protected long trailTime;
	protected float size;
	
	public TrailPen(float size){
		this.size=size;
		points.add(new TrailPoint(new Vector3f(0,size,0), new Vector3f(0,-1,0)));
		points.add(new TrailPoint(new Vector3f(0,-size,0), new Vector3f(0,1,0)));
	}
	
	public boolean needsDelete(long t, float max){
		return t-trailTime>max;
	}
	
	public TrailPen clone(){
		return new TrailPen(size);
	}
	
	public void update(long time){
		long t=time-trailTime;
		
		for(TrailPoint p:points){
			
		}
	}
} 
