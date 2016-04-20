package com.entity.test;

import com.entity.anot.Entity;
import com.entity.anot.RayPick;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.items.Model;

@ModelEntity
public abstract  class BasicModel2 extends Model {
	@Entity(conditional="test")
	public BasicModel model1;
	
	
	public boolean test(Boolean value){
		System.out.println("test???"+value);
		return value;
	}
	

}
