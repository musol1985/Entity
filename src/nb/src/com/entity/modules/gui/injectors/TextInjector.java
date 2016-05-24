/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.modules.gui.injectors;

import java.lang.reflect.Field;

import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;
import com.entity.modules.gui.anot.TextGUI;
import com.entity.modules.gui.beans.TextBean;
import com.entity.modules.gui.items.Text;

/**
 *
 * @author Edu
 */
public class TextInjector<T  extends IEntity>  extends ListBeanInjector<TextBean, T>{


    @Override
    public void loadField(Class<T> c, Field f) throws Exception {
    	if(EntityManager.isAnnotationPresent(TextGUI.class,f)){
			beans.add(new TextBean(c, f, TextGUI.class));
		}
    }

	@Override
	public void onInstance(T item, IBuilder builder, Object[] params) throws Exception {
		for(TextBean bean:beans){
            Text txt=bean.create();
            bean.initialize(item, txt);
            
            bean.getField().set(item, txt);
        }
	}
}
