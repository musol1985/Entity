/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.modules.gui.items;



import com.entity.modules.gui.anot.SpriteGUI;
import com.entity.modules.gui.events.IOnMouseMove;
import com.entity.modules.gui.events.MoveEvent;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author Edu
 */
public class Button extends Sprite implements IOnMouseMove{
	private String imgBack;
	private String imgHover;
	private String imgDisabled;
	
	private boolean enabled;
	
	@SpriteGUI(name="buttonIcon")
	public Sprite icon;
    
    public void instance(String name,  String imgBack, String imgHover, String imgDisabled, boolean enabled) {
    	super.instance(name, new Geometry(name+"geo", new Quad(1,1,false)), imgBack);
    	
    	this.imgBack=imgBack;
    	this.imgHover=imgHover;
    	this.imgDisabled=imgDisabled;
    	setEnabled(enabled);
    }
    
    public void setIcon(String iconImg){    	
    	icon.setImage(iconImg, true);
    	icon.centerInParent();
    }
    
    public void setEnabled(boolean enabled){
    	this.enabled=enabled;
    	if(enabled){
    		setImage(imgBack, true);
    		icon.setAlpha(0.5f);
    	}else{
    		setImage(imgDisabled, true);
    		icon.setAlpha(1f);
    	}
    }

	@Override
	public void onIn(MoveEvent event) throws Exception {
		if(enabled)
			setImage(imgHover, true);
	}

	@Override
	public void onOut(MoveEvent event) throws Exception {
		if(enabled){
    		setImage(imgBack, true);
    	}else{
    		setImage(imgDisabled, true);
    	}
	}

	@Override
	public void onMove(MoveEvent event) throws Exception {
		
	}
    
}
