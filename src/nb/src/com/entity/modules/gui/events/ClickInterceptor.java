package com.entity.modules.gui.events;

import java.lang.reflect.Method;

import com.entity.modules.gui.anot.SpriteGUI;
import com.entity.modules.gui.items.SpriteBase;
import com.entity.modules.gui.items.SpriteBase.BUTTON;

public class ClickInterceptor {
	private Object obj;
	private Method leftClick;
	private Method rightClick;
	
	public static boolean isClickInterceptor(SpriteGUI anot){
		return !anot.onLeftClick().isEmpty();
	}
	
	public boolean onClick(ClickEvent event, SpriteBase sprt)throws Exception{
		if(event.button==BUTTON.LEFT){
	    	leftClick.invoke(obj, sprt, event);
    	}
		return false;
	}
	
	public ClickInterceptor(Object obj){
		this.obj=obj;
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public Method getLeftClick() {
		return leftClick;
	}
	public void setLeftClick(Method leftClick) {
		this.leftClick = leftClick;
	}
	public Method getRightClick() {
		return rightClick;
	}
	public void setRightClick(Method rightClick) {
		this.rightClick = rightClick;
	}
	
	
}
