package com.entity.adapters.modifiers;

import com.entity.adapters.Modifier;
import com.entity.adapters.bean.ModifierValueBean;
import com.entity.adapters.listeners.IModifierOnFinish;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class ModifierPosition<S extends Spatial> extends Modifier<S>{

	
	public ModifierPosition(Vector3f from, Vector3f to, float time) {
		this(from, to, time, false, null);
	}
	
	public ModifierPosition(Vector3f from, Vector3f to, float time, IModifierOnFinish listener) {
		this(from, to, time, false, listener);
	}

	public ModifierPosition(Vector3f from, Vector3f to, float time, boolean loop, IModifierOnFinish listener) {
		super(time, loop, new ModifierValueBean[]{
				new ModifierValueBean(from.x, to.x, time),
				new ModifierValueBean(from.y, to.y, time),
				new ModifierValueBean(from.z, to.z, time)
		});
		setFinishListener(listener);

	}

	@Override
	public void onUpdate(float tpf, ModifierValueBean[] values) {
		s.setLocalTranslation(values[0].getValue(), values[1].getValue(), values[2].getValue());
	}

}
