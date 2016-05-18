package com.entity.modules.gui.beans;

import java.lang.reflect.Field;

import com.entity.bean.AnnotationFieldBean;
import com.entity.core.IEntity;
import com.entity.modules.gui.anot.ButtonGUI;
import com.entity.modules.gui.items.Button;

public class ButtonBean extends AnnotationFieldBean<ButtonGUI>{
	private SpriteBean bean;
	
	public ButtonBean(Class c, Field f, Class<ButtonGUI> a)throws Exception{
		super(f, a);			
		bean=new SpriteBean(c, f, annot.sprite());
	}
	
	
	public Button create(IEntity parent)throws Exception{
		Button button=(Button)instanceEntity();

		button.instance(bean.getAnnot().name(), annot.imgBack(), annot.imgHover(), annot.imgDisabled(), annot.icon(), annot.enabled());
		
		bean.initialize(parent, button);
        
        return button;
	}
}
