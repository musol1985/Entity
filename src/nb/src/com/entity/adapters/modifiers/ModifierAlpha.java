package com.entity.adapters.modifiers;

import com.entity.adapters.Modifier;
import com.entity.adapters.bean.ModifierValueBean;
import com.entity.adapters.listeners.IModifierOnFinish;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

public class ModifierAlpha<T extends Spatial> extends Modifier<T>{
	private ColorRGBA color;
	
	public ModifierAlpha(float from, float to, float time) {
		this(from, to, time, null);
	}
	
	public ModifierAlpha(float from, float to, float time, IModifierOnFinish listener) {
		this(from, to, time, false, listener);
	}

	public ModifierAlpha(float from, float to, float time, boolean loop, IModifierOnFinish listener) {
		super(time, loop, new ModifierValueBean[]{
				new ModifierValueBean(from, to, time)
		});
		setFinishListener(listener);
	}
	
	
	
	@Override
	public void setSpatial(Spatial s) {
		super.setSpatial(s);
		if(s!=null){
			color=(ColorRGBA)((Geometry)s).getMaterial().getParam("Diffuse").getValue();
		}
	}

	@Override
	public void onUpdate(float tpf, ModifierValueBean[] values) {
		color.a=values[0].getValue();
		((Geometry)s).getMaterial().setColor("Diffuse", color);			
	}

}
