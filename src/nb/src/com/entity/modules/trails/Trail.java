package com.entity.modules.trails;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class Trail extends Geometry implements Control{
	private List<TrailSegment> segments=new ArrayList<TrailSegment>();
	private Vector3f firstPosRender;
	private Vector3f lastPosRender;
	private TrailPen pen;
	
	private float trailTime;
	private float penSize;
	private float penSpeed;
	private int minSegmentsDistance=1;
	
	public Trail(String name, TrailPen pen){
		this(name, 5, 10*1000, pen);
	}
	
	public Trail(String name, float penSize, float trailTime, TrailPen pen){
		super(name);
		this.pen=pen;
		this.penSize=penSize;
		this.trailTime=trailTime;
		this.penSpeed=penSize/trailTime;
		
		this.segments.add(new TrailSegment(penSize, pen));
		this.mesh=new Mesh();
	}

	@Override
	public Control cloneForSpatial(Spatial paramSpatial) {
		return null;
	}

	@Override
	public void render(RenderManager paramRenderManager, ViewPort paramViewPort) {
		if(lastPosRender==null){			
			lastPosRender=getWorldTranslation();
		}else{
			firstPosRender=getWorldTranslation();

			draw(firstPosRender.distance(lastPosRender)>minSegmentsDistance);
		}
	}

	@Override
	public void setSpatial(Spatial paramSpatial) {
		
	}

	@Override
	public void update(float tpf) {
		
	}
	
	
	private void draw(boolean newSegment){
		long time=System.currentTimeMillis();
		
		if(segments.get(0).needsDelete(time, trailTime)){
			segments.remove(0);
		}
		
		if(newSegment){
			segments.add(new TrailSegment(penSize, pen));
		}
		
		//float[] vertices=new float[pen.points.size()*segments.size()*3];
		System.out.println("Draw "+time);
		for(TrailSegment pen:segments){
			System.out.println("++Segment "+pen);
			for(TrailPoint p:pen.points){
				System.out.println("----->PrePoint"+p.pos.toString());
				pen.updatePoint(p, time, penSpeed);
				System.out.println("----->PostPoint"+p.pos.toString());
			}
		}
		
		
	}
}
