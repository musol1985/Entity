package com.entity.adapters.listeners;

import com.entity.adapters.Modifier;

public interface IModifierOnUpdate<T extends Modifier> {
	public void onUpdate(T m, float tpf);
}
