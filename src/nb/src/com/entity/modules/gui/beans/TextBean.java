package com.entity.modules.gui.beans;

import java.lang.reflect.Field;

import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.modules.gui.anot.TextGUI;
import com.entity.modules.gui.items.Text;
import com.entity.utils.Utils;

public class TextBean extends AnnotationFieldBean<TextGUI>{

	
	public TextBean(Class c, Field f, TextGUI a)throws Exception{
		super(f, a);
	}
	
	public TextBean(Class c, Field f, Class<TextGUI> a)throws Exception{
		this(c, f, EntityManager.getAnnotation(a,f));			
	}


	
	public Text create()throws Exception{
		Text sprite=(Text)instanceEntity();
    
        sprite.instance(getAnnot().name(), EntityManager.getGame().getAssetManager().loadFont(getAnnot().font()), getAnnot().align());
        sprite.setText(annot.text());
        sprite.setColor(Utils.getColorFromFloats(annot.color()));

        return sprite;
	}
	
	public void initialize(IEntity parent, Text txt)throws Exception{		
		txt.setPosition(getAnnot().position()[0], getAnnot().position()[1]);

        if(getAnnot().attach()){
        	txt.attachToParent(parent);
        }
	}
}
