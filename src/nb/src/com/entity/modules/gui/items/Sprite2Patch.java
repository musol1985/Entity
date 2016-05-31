/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.modules.gui.items;



import com.entity.core.EntityManager;
import com.entity.modules.gui.anot.SpriteGUI.ALIGN;
import com.entity.modules.gui.items.mesh.Quad2Patch;
import com.jme3.asset.TextureKey;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;

/**
 *
 * @author Edu
 */
public class Sprite2Patch<P extends SpriteBase> extends Sprite<P>{
	private Quad2Patch mesh;
	
	public void instance(String name,  String texture, ALIGN align, float offset){
		mesh=new Quad2Patch(offset, getImageWidth(texture));
		super.instance(name, new Geometry(name+"geo", mesh), texture, align);
	}

    public float getWidth() {
        return (width/getGUI().ratioW);
    }

    public float getHeight() {
        return (height/getGUI().ratioH);
    }
    
    public float getImageWidth(String image){
        TextureKey key = new TextureKey(image, true);
        Texture2D tex = (Texture2D) EntityManager.getAssetManager().loadTexture(key);
        return tex.getImage().getWidth();
    }
    
    public void setImage(String imgName, boolean useAlpha){
        setName(imgName);
        TextureKey key = new TextureKey(imgName, true);
        Texture2D tex = (Texture2D) EntityManager.getAssetManager().loadTexture(key);
        
        //tex.setWrap(Texture.WrapMode.MirroredRepeat);
        
        setWidth(tex.getImage().getWidth());
        setHeight(tex.getImage().getHeight());
        
        setTexture(tex, useAlpha);
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
