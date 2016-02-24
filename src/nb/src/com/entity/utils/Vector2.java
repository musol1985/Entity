/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.utils;

import java.io.Serializable;

/**
 *
 * @author Edu
 */
@com.jme3.network.serializing.Serializable
public class Vector2 implements Serializable{
    public int x;
    public int z;

    public Vector2(int x, int z) {
        this.x = x;
        this.z = z;
    }
    
    public Vector2() {
        this.x = 0;
        this.z = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
    
    public Vector2 add(int x, int z){
        this.x+=x;
        this.z+=z;
        return this;
    }
    
    public Vector2 addX(int x){
        this.x+=x;
        return this;
    }
    
    public Vector2 addZ(int z){
        this.z+=z;
        return this;
    }
    
    public boolean igual(Vector2 v){
        return v.x==x && v.z==z;
    }
    
    @Override
    public String toString(){
        return x+","+z;
    }
    
    @Override
    public Vector2 clone(){
        return new Vector2(x,z);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2 other = (Vector2) obj;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

}