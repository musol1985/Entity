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
import com.entity.modules.gui.anot.SpriteGUI;
import com.entity.modules.gui.beans.SpriteBean;
import com.entity.modules.gui.items.Sprite;

/**
 *
 * @author Edu
 */
public class SpriteInjector<T  extends IEntity>  extends ListBeanInjector<SpriteBean, T>{


    @Override
    public void loadField(Class<T> c, Field f) throws Exception {
    	if(EntityManager.isAnnotationPresent(SpriteGUI.class,f)){
			beans.add(new SpriteBean(c, f, SpriteGUI.class));
		}
    }

	@Override
	public void onInstance(T item, IBuilder builder, Object[] params) throws Exception {
		for(SpriteBean bean:beans){
            Sprite sprite=bean.create();
            bean.initialize(item, sprite);
            
            bean.getField().set(item, sprite);
        }
	}
}
