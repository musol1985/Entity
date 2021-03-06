package com.entity.core.injectors.input;

import java.lang.reflect.Method;

import com.entity.anot.components.input.Input;
import com.entity.bean.custom.InputBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;

public class InputInjector<T extends IEntity>  extends ListBeanInjector<InputBean, T>{	

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {
		if(EntityManager.isAnnotationPresent(Input.class,m)){
			log.info("Input annotation detected : "+m.getParameterTypes().length+" "+m.getParameterTypes()[0]);
			beans.add(new InputBean(m, Input.class));
		}
	}

	

	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(final InputBean bean: beans){
			if(conditional(e, bean.getMethod(), params)){
				if(bean.isDigital()){								
					InputListener listener=new ActionListener() {
						public void onAction(String arg0, boolean arg1, float arg2) {
							try {
								/*if(bean.getAnnot().guiLeftButton()){
									if(!EntityManager.getGame().getClickInterceptor().onLeftClick(arg1, arg2)){
										bean.getMethod().invoke(e, arg1, arg2);
									}
								}else if(bean.getAnnot().guiRightButton()){
									if(!EntityManager.getGame().getClickInterceptor().onRightClick(arg1, arg2)){
										bean.getMethod().invoke(e, arg1, arg2);
									}
								}else{*/
	                                bean.invoke(e, arg1, arg2);
									//bean.getMethod().invoke(e, arg1, arg2);
								//}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					log.info("Registering digitalInput listener for "+ bean.getActionName(e));
					EntityManager.getInputManager().addListener(listener,bean.getActionName(e));	
				}else{
					InputListener listener=new AnalogListener() {
						public void onAnalog(String arg0, float arg1, float tpf) {
							try {
								bean.invoke(e, arg1, tpf);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}	
					};
					log.info("Registering analogInput listener for "+bean.getActionName(e));
					EntityManager.getInputManager().addListener(listener, bean.getActionName(e));	
				}
			}
		}
		
	}
	
}
