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
import com.entity.modules.gui.anot.ButtonGUI;
import com.entity.modules.gui.beans.ButtonBean;
import com.entity.modules.gui.items.Button;

/**
 *
 * @author Edu
 */
public class ButtonInjector<T  extends IEntity>  extends ListBeanInjector<ButtonBean, T>{


    @Override
    public void loadField(Class<T> c, Field f) throws Exception {
    	if(EntityManager.isAnnotationPresent(ButtonGUI.class,f)){
			beans.add(new ButtonBean(c, f, ButtonGUI.class));
		}
    }

	@Override
	public void onInstance(T item, IBuilder builder, Object[] params) throws Exception {
		for(ButtonBean bean:beans){
            Button button=bean.create(item);
            
            bean.getField().set(item, button);
        }
	}
}
