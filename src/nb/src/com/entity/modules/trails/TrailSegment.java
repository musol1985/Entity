package com.entity.modules.trails;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;

public class TrailSegment {
	protected List<TrailPoint> points;
	protected long penTime;

	
	public TrailSegment(float size, TrailPen pen){
		points=pen.generatePoints(size);
		penTime=System.currentTimeMillis();
	}
	
	public boolean needsDelete(long t, float max){
		return t-penTime>max;
	}

	
	public void updatePoint(TrailPoint p, long time, float v){
		float x=(time-penTime)*v;
		System.out.println("----------------------------Time: "+(time-penTime));
		p.pos.add(p.reductionDirection.mult(x));
	}
} 
