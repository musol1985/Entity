/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.modules.gui.items;



import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author Edu
 */
public class Sprite extends SpriteBase{
    
    public void instance(String name,  String texture) {
    	super.instance(name, new Geometry(name+"geo", new Quad(1,1,false)), texture);
    }
    
}
