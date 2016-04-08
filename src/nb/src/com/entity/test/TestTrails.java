package com.entity.test;

import com.entity.modules.trails.Trail;
import com.entity.modules.trails.TrailPen;
import com.jme3.math.Vector3f;

public class TestTrails {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Trail t=new Trail("t",new TrailPen());
		
		t.setLocalTranslation(new Vector3f(0,0,0));
		t.render(null, null);

		Thread.sleep(500);
		
		t.setLocalTranslation(new Vector3f(5,0,0));
		t.render(null, null);

		Thread.sleep(500);
		
		t.setLocalTranslation(new Vector3f(10,0,0));
		t.render(null, null);
		
		Thread.sleep(500);
		
		t.setLocalTranslation(new Vector3f(15,0,0));
		t.render(null, null);
	}

}
