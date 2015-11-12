package com.entity.adapters;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class ControlAdapter implements Control{

	public void read(JmeImporter arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void write(JmeExporter arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public Control cloneForSpatial(Spatial arg0) {
		return new ControlAdapter();
	}

	public void render(RenderManager arg0, ViewPort arg1) {
		// TODO Auto-generated method stub
		
	}

	public void setSpatial(Spatial arg0) {
		// TODO Auto-generated method stub
		
	}

	public void update(float arg0) {
		// TODO Auto-generated method stub
		
	}


}
