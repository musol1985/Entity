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
import com.entity.modules.gui.anot.SpriteGUI;
import com.entity.modules.gui.beans.ClickInterceptorBean;
import com.entity.modules.gui.items.Sprite;

/**
 *
 * @author Edu
 */
public class SpriteInjector<T  extends IEntity>  extends ListBeanInjector<ClickInterceptorBean, T>{


    @Override
    public void loadField(Class<T> c, Field f) throws Exception {
    	if(EntityManager.isAnnotationPresent(SpriteGUI.class,f)){
			beans.add(new ClickInterceptorBean(c, f, SpriteGUI.class));
		}
    }

	@Override
	public void onInstance(T item, IBuilder builder, Object[] params) throws Exception {
		for(ClickInterceptorBean bean:beans){
            Sprite sprite=(Sprite)bean.instanceEntity();
            
            sprite.instance(bean.getAnnot().name(), bean.getAnnot().texture());
            sprite.setInterceptor(bean.getInterceptor(item));
            
            bean.getField().set(item, sprite);
            
            sprite.setPosition(bean.getAnnot().position()[0], bean.getAnnot().position()[1]);

            if(bean.getAnnot().attach()){
                sprite.attachToParent(item);
            }
        }
	}
}
