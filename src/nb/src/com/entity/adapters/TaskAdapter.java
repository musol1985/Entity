package com.entity.adapters;

import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.core.items.Scene;

public abstract class TaskAdapter<T extends Scene, E extends IEntity> implements Runnable {

	public T getScene(){
		return (T)EntityManager.getCurrentScene();
	}
	
	public abstract void onCreate(E entity)throws Exception;
}
