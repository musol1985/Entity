/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.modules.gui.injectors;

import java.lang.reflect.Field;

import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;
import com.entity.modules.gui.anot.ScreenGUI;
import com.entity.modules.gui.items.Screen;

/**
 *
 * @author Edu
 */
public class ScreenInjector<T  extends IEntity>  extends ListBeanInjector<AnnotationFieldBean<ScreenGUI>, T>{

	@Override
    public void loadField(Class<T> c, Field f) throws Exception {
    	if(EntityManager.isAnnotationPresent(ScreenGUI.class,f)){
			beans.add(new AnnotationFieldBean<ScreenGUI>(f, ScreenGUI.class));
		}
    }
	
	@Override
	public void onInstance(T item, IBuilder builder, Object[] params) throws Exception {
		for(AnnotationFieldBean<ScreenGUI> bean:beans){
            Screen s=(Screen)bean.instanceEntity();
            
            bean.getField().set(item, s);
            
            if(bean.getAnnot().attach())
            	EntityManager.getGame().getGUI().setScreen(s);
        }
	}

    
}
