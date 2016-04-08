package com.entity.modules.trails;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;

public class TrailPen {

	public List<TrailPoint> generatePoints(float size){
		List<TrailPoint> points=new ArrayList<TrailPoint>();
		
		points.add(new TrailPoint(new Vector3f(0,size,0), new Vector3f(0,-1,0)));
		points.add(new TrailPoint(new Vector3f(0,-size,0), new Vector3f(0,1,0)));
		
		return points;
	}
} 
