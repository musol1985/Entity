package com.entity.modules.gui.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.modules.gui.anot.SpriteGUI;
import com.entity.modules.gui.events.ClickEvent;
import com.entity.modules.gui.events.ClickInterceptor;
import com.entity.modules.gui.items.Sprite;

public class SpriteBean extends AnnotationFieldBean<SpriteGUI>{
	private Method leftClick;
	private Method rightClick;
	
	public SpriteBean(Class c, Field f, SpriteGUI a)throws Exception{
		super(f, a);
		
		if(!annot.onLeftClick().isEmpty()){
			leftClick=c.getDeclaredMethod(annot.onLeftClick(), new Class[]{Sprite.class, ClickEvent.class});
		}
		if(!annot.onRightClick().isEmpty()){
			leftClick=c.getDeclaredMethod(annot.onRightClick(), new Class[]{Sprite.class, ClickEvent.class});
		}
	}
	
	public SpriteBean(Class c, Field f, Class<SpriteGUI> a)throws Exception{
		this(c, f, EntityManager.getAnnotation(a,f));			
	}

	public ClickInterceptor getInterceptor(Object obj)throws Exception{
		ClickInterceptor i= new ClickInterceptor(obj);
		i.setLeftClick(leftClick);
		i.setRightClick(rightClick);
		return i;
	}
	
	public Sprite create()throws Exception{
            Sprite sprite=(Sprite)instanceEntity();
        
            sprite.instance(getAnnot().name(), getAnnot().texture(), getAnnot().align());

            return sprite;
	}
	
	public void initialize(IEntity parent, Sprite sprite)throws Exception{
		
        sprite.setInterceptor(getInterceptor(parent));

        sprite.setPosition(getAnnot().position()[0], getAnnot().position()[1]);

        if(getAnnot().attach()){
            sprite.attachToParent(parent);
        }
	}
}
