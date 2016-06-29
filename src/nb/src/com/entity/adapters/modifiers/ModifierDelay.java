package com.entity.adapters.modifiers;

import com.entity.adapters.Modifier;
import com.entity.adapters.bean.ModifierValueBean;
import com.entity.adapters.listeners.IModifierOnFinish;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;

public class ModifierDelay<T extends Spatial> extends Modifier<T>{
	private ColorRGBA color;
	
	public ModifierDelay(float time) {
		this(time, null);
	}
	
	public ModifierDelay(float time, IModifierOnFinish listener) {
		this(0, 100, time, false, listener);
	}

	public ModifierDelay(float from, float to, float time, boolean loop, IModifierOnFinish listener) {
		super(time, loop, new ModifierValueBean[]{
				new ModifierValueBean(from, to, time)
		});
		setFinishListener(listener);
	}


	@Override
	public void onUpdate(float tpf, ModifierValueBean[] values) {
	
	}

}
