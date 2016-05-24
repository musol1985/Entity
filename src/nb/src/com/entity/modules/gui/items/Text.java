/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.modules.gui.items;



import com.entity.anot.BuilderDefinition;
import com.entity.modules.gui.anot.SpriteGUI.ALIGN;
import com.entity.modules.gui.builders.TextBuilder;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author Edu
 */
@BuilderDefinition(builderClass=TextBuilder.class)
public class Text<Parent extends SpriteBase> extends SpriteBase<BitmapText, Parent>{
    
    public void instance(String name,  BitmapFont font, ALIGN align) {
    	super.instance(name, new BitmapText(font, false), align);
    	
    	geo.setSize(font.getCharSet().getRenderedSize());
    }
    
    public void setTextColor(ColorRGBA color){
    	geo.setColor(color);
    }
    
    public void setText(String txt){
    	geo.setText(txt);
    }
    
    public String getText(){
    	return geo.getText();
    }

    @Override
    public float getHeight() {
        return geo.getHeight(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setPosition(int x, int y) {
        setLocalTranslation(x*getGUI().ratioW, y*getGUI().ratioH+getHeight(), getLocalTranslation().z);      
    }

}
