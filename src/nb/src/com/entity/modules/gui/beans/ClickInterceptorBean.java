package com.entity.modules.gui.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.bean.AnnotationFieldBean;
import com.entity.modules.gui.anot.SpriteGUI;
import com.entity.modules.gui.events.ClickEvent;
import com.entity.modules.gui.events.ClickInterceptor;
import com.entity.modules.gui.items.Sprite;

public class ClickInterceptorBean extends AnnotationFieldBean<SpriteGUI>{
	private Method leftClick;
	private Method rightClick;
	
	public ClickInterceptorBean(Class c, Field f, Class<SpriteGUI> a)throws Exception{
		super(f, a);			
		
		if(!annot.onLeftClick().isEmpty()){
			leftClick=c.getDeclaredMethod(annot.onLeftClick(), new Class[]{Sprite.class, ClickEvent.class});
		}
		if(!annot.onRightClick().isEmpty()){
			leftClick=c.getDeclaredMethod(annot.onRightClick(), new Class[]{Sprite.class, ClickEvent.class});
		}
	}

	public ClickInterceptor getInterceptor(Object obj)throws Exception{
		ClickInterceptor i= new ClickInterceptor(obj);
		i.setLeftClick(leftClick);
		i.setRightClick(rightClick);
		return i;
	}
}
