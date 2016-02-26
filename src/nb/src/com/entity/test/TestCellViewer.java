package com.entity.test;

import com.entity.utils.Vector2;
import com.jme3.math.Vector3f;

public class TestCellViewer {
	public static final int CELL_SIZE=10;
	public static final int VRTUAL_SIZE=CELL_SIZE/2;
	
	
	public static void main(String[] args)throws Exception{
		test(0,0);
	}
	
	
	public static void test(float x, float z){
		System.out.println("Input: "+x+","+z);
		System.out.println("		CELL "+getCellPosByReal(new Vector3f(x,0,z)));
		System.out.println("		VIRT "+getVirtualPosByReal(new Vector3f(x,0,z)));
		System.out.println("################");
	}
	
	
	public static Vector2 getCellPosByReal(Vector3f pos){
		return new Vector2((int)pos.x/CELL_SIZE,(int)pos.z/CELL_SIZE);
	}
	
	public static Vector2 getVirtualPosByReal(Vector3f pos){
		return new Vector2((int)pos.x/VRTUAL_SIZE,(int)pos.z/VRTUAL_SIZE);
	}
}
