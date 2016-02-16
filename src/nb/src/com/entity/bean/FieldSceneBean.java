package com.entity.bean;

import java.lang.reflect.Field;

import com.entity.anot.entities.SceneEntity;
import com.entity.core.EntityGame;
import com.entity.core.items.Scene;

public class FieldSceneBean {
	private EntityGame g;
	private Field f;
	private SceneEntity anot;
	
	
	
	
	public FieldSceneBean(Field f, EntityGame g, SceneEntity anot) {
		this.g = g;
		this.f=f;
		this.anot=anot;
	}




	public EntityGame getG() {
		return g;
	}




	public Field getF() {
		return f;
	}




	public SceneEntity getAnot() {
		return anot;
	}

	
	
}
