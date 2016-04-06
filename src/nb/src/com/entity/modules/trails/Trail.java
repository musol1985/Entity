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
	private List<TrailPen> segments=new ArrayList<TrailPen>();
	private Vector3f firstPosRender;
	private Vector3f lastPosRender;
	private TrailPen pen;
	
	private float trailTime=5*1000;
	private int minSegmentsDistance=1;
	
	public Trail(String name, TrailPen pen){
		super(name);
		this.pen=pen;
		this.segments.add(pen.clone());
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
			TrailPen p=pen.clone();
			p.trailTime=System.currentTimeMillis();
			segments.add(p);
		}
		
		float[] vertices=new float[pen.points.size()*segments.size()*3];
		
		for(TrailPen pen:segments){
			
		}
		
		
	}
}
