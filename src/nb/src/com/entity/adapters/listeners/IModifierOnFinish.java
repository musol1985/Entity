package com.entity.adapters.listeners;

import com.entity.adapters.Modifier;
import com.jme3.scene.Spatial;

public interface IModifierOnFinish<T extends Modifier, S extends Spatial> {
	public void onFinish(T m, S model);
}
