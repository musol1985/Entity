package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.components.input.Input;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;

public class InputInjector<T extends IEntity>  extends BaseInjector<T>{
	private List<Method> inputsDigital=new ArrayList<Method>();
	private List<Method> inputsAnalog=new ArrayList<Method>();


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(final Method m:inputsAnalog){			
			InputListener listener=new AnalogListener() {
				public void onAnalog(String arg0, float arg1, float tpf) {
					try {
						m.invoke(e, arg1, tpf);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
			};
			System.out.println("Registering analogInput listener for "+m.getAnnotation(Input.class).action());
			EntityManager.getInputManager().addListener(listener, m.getAnnotation(Input.class).action());	
		}
		
		for(final Method m:inputsDigital){	
			final Input anot=m.getAnnotation(Input.class);
			
			InputListener listener=new ActionListener() {
				public void onAction(String arg0, boolean arg1, float arg2) {
					try {
						if(anot.guiLeftButton()){
							if(!EntityManager.getGame().getClickInterceptor().onLeftClick(arg1, arg2)){
								m.invoke(e, arg1, arg2);
							}
						}else if(anot.guiRightButton()){
							if(!EntityManager.getGame().getClickInterceptor().onRightClick(arg1, arg2)){
								m.invoke(e, arg1, arg2);
							}
						}else{
							m.invoke(e, arg1, arg2);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			System.out.println("Registering digitalInput listener for "+anot.action());
			EntityManager.getInputManager().addListener(listener, anot.action());	
		}
		
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {

	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {
		if(m.isAnnotationPresent(Input.class)){
			System.out.println("Input annotation detected : "+m.getParameterTypes().length+" "+m.getParameterTypes()[0]);
			if(m.getParameterTypes().length==2 && m.getParameterTypes()[0]==boolean.class){
				inputsDigital.add(m);
			}else{
				inputsAnalog.add(m);
			}
		}
	}

	@Override
	public boolean hasInjections() {
		return inputsDigital.size()>0 || inputsAnalog.size()>0;
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
