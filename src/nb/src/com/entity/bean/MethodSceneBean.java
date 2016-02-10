package com.entity.bean;

import java.lang.reflect.Method;

import com.entity.anot.entities.SceneEntity;
import com.entity.core.items.Scene;

public class MethodSceneBean {
	private Scene s;
	private SceneEntity anot;
	
	
	
	
	public MethodSceneBean(Scene s, SceneEntity anot) {
		this.s = s;
		this.anot=anot;
	}

	public Scene getS() {
		return s;
	}
	public void setS(Scene s) {
		this.s = s;
	}
	public SceneEntity getAnot() {
		return anot;
	}
	public void setAnot(SceneEntity anot) {
		this.anot = anot;
	}
	
	
}
