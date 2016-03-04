/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.adapters;

import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.jme3.light.DirectionalLight;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 *
 * @author Edu
 */
public class DirectionalLightShadow extends DirectionalLight{
	 public DirectionalLightShadowRenderer shadow;

     public DirectionalLightShadow(int mapSize, int splits, float intensity){
    	 shadow = new DirectionalLightShadowRenderer(EntityManager.getAssetManager(), mapSize, splits);
    	 shadow.setLight(this);
    	 shadow.setShadowIntensity(intensity);
     }

	public DirectionalLightShadowRenderer getShadow() {
		return shadow;
	}
     
    public void attachShadow(EntityGame g){
    	g.getViewPort().addProcessor(shadow);
    }
    
    public void dettachShadow(EntityGame g){
    	g.getViewPort().removeProcessor(shadow);
    }
}
