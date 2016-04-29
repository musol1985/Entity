package com.entity.modules.gui.events;

import com.entity.core.items.ModelBase;
import com.entity.modules.gui.items.SpriteBase.BUTTON;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;

public class ClickEvent {
	public BUTTON button;
	public boolean value;
	public float tpf;
	public Vector2f pos;
	
	
	public ClickEvent(BUTTON button, boolean value, float tpf, Vector2f pos) {
		this.button = button;
		this.value = value;
		this.tpf = tpf;
		this.pos=pos;
	}


	public BUTTON getButton() {
		return button;
	}


	public void setButton(BUTTON button) {
		this.button = button;
	}


	public boolean isValue() {
		return value;
	}


	public void setValue(boolean value) {
		this.value = value;
	}


	public float getTpf() {
		return tpf;
	}


	public void setTpf(float tpf) {
		this.tpf = tpf;
	}


	public Vector2f getPos() {
		return pos;
	}


	public void setPos(Vector2f pos) {
		this.pos = pos;
	}
	
	public boolean click(ModelBase model){
		switch(button){
			case LEFT:
				return ((IOnLeftClick)model).onLeftClick(value, tpf);
			case RIGHT:
				return ((IOnRightClick)model).onRightClick(value, tpf);
			case MIDDLE:
				return ((IOnMiddleClick)model).onMiddleClick(value, tpf);
		}
		return false;
	}
	
	
	public boolean isClickable(Spatial m){
		if(button==BUTTON.LEFT && m instanceof IOnLeftClick)return true;
		if(button==BUTTON.RIGHT && m instanceof IOnRightClick)return true;
		if(button==BUTTON.MIDDLE && m instanceof IOnMiddleClick)return true;
		return false;
	}
}
