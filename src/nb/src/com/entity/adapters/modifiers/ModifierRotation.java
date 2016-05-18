package com.entity.adapters.modifiers;

import com.entity.adapters.Modifier;
import com.entity.adapters.bean.ModifierValueBean;
import com.entity.adapters.listeners.IModifierOnFinish;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class ModifierRotation<T extends Spatial> extends Modifier<T>{

	
	/*public ModifierRotation(T from, Vector3f to, float time) {
		this(from, to, time, null);
	}*/
	
	/*public ModifierRotation(T from, Vector3f to, float time, IModifierOnFinish listener) {
		this(getRotation(from), to, time, false, listener);
	}*/
        
        public ModifierRotation(Vector3f from, Vector3f to, float time, IModifierOnFinish listener) {
		this(from, to, time, false, listener);
	}
        
        private Vector3f getRotation(T item){
            float[] v=(item.getLocalRotation().toAngles(new float[3]));
            return new Vector3f(v[0], v[1], v[2]);
        }

	public ModifierRotation(Vector3f from, Vector3f to, float time, boolean loop, IModifierOnFinish listener) {
		super(time, loop, new ModifierValueBean[]{
				new ModifierValueBean(from.x, to.x, time),
				new ModifierValueBean(from.y, to.y, time),
				new ModifierValueBean(from.z, to.z, time)
		});
		setFinishListener(listener);

	}
	
	@Override
	public void onUpdate(float tpf, ModifierValueBean[] values) {
		s.setLocalRotation(new Quaternion().fromAngles(values[0].getValue(), values[1].getValue(), values[2].getValue()));		
	}

}
