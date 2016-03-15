package com.entity.bean.custom;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.entity.anot.components.model.VehicleComponent;
import com.entity.anot.components.model.WheelComponent;
import com.entity.bean.BodyBean;
import com.entity.core.items.ModelBase;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.math.Vector3f;

public class VehicleBean extends BodyBean<VehicleComponent>{
	private List<WheelComponent> wheels;
	
	public VehicleBean(Class c, Field f)throws Exception{
		super(c, f, VehicleComponent.class);
		
		wheels=new ArrayList<WheelComponent>(Arrays.asList(annot.wheels()));
	}

	public List<WheelComponent> getWheels() {
		return wheels;
	}
	
	public void addWheels(VehicleControl v, ModelBase model){
		for(WheelComponent wheel:wheels){
			addWheel(v, model, wheel);
		}
	}
	
	private void addWheel(VehicleControl v, ModelBase model, WheelComponent wheel){	
		Vector3f offset=arrayToVector(wheel.offset());
		Vector3f direction=arrayToVector(wheel.direction());
		Vector3f axe=arrayToVector(wheel.axe());
		
		v.addWheel(model.getChild(wheel.nodeName()), offset,
                direction, axe, wheel.restLength(), wheel.radius(), wheel.frontWheel());
	}
	
	private Vector3f arrayToVector(float[] array){
		return new Vector3f(array[0],array[1],array[2]);
	}
}
