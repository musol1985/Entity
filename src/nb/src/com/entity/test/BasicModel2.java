package com.entity.test;

import com.entity.anot.Entity;
import com.entity.anot.RayPick;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.items.Model;

@ModelEntity
public class BasicModel2 extends Model implements Runnable{
	@Entity
	private BasicModel model1;
	
	
	public void test(){
		System.out.println(model1+" "+(model1!=null));
	}
	
	@RayPick()
	public void t(){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
