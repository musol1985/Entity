/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.modules.gui.items;



import com.entity.modules.gui.anot.SpriteGUI.ALIGN;
import com.entity.modules.gui.items.mesh.Quad2Patch;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author Edu
 */
public class Sprite2Patch<P extends SpriteBase> extends Sprite<P>{
	private Quad2Patch mesh;
	
	public void instance(String name,  String texture, ALIGN align, float offset){
		mesh=new Quad2Patch(offset);
		super.instance(name, new Geometry(name+"geo", mesh), texture, align);
	}

    public float getWidth() {
        return (width/getGUI().ratioW);
    }

    public float getHeight() {
        return (height/getGUI().ratioH);
    }
    
    public void setWidth(float width){
        width=width*getGUI().ratioW;
        this.width = width;
        //geo.setLocalScale(new Vector3f(width, height, 1f));
        mesh.updateSize(width, height);
        geo.updateModelBound();
    }
    
    public void setHeight(float height){
        height=height*getGUI().ratioH;
        this.height = height;
        mesh.updateSize(width, height);
        geo.updateModelBound();
        //geo.setLocalScale(new Vector3f(width, height, 1f));
    }
}
