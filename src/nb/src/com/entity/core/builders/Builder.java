package com.entity.core.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.entity.bean.MaterialBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.BaseInjector;
import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Node;

public abstract class Builder<T extends IEntity> implements IBuilder<T>{
	private static final Logger log = Logger.getLogger(Builder.class.getName());
	
	private HashMap<Class<BaseInjector>, BaseInjector> injectors=new HashMap<Class<BaseInjector>, BaseInjector>();
	private List<BaseInjector> usedInjectors=new ArrayList<BaseInjector>();
	private List<InjectorAttachable> attachableInjectors=new ArrayList<InjectorAttachable>();
	
	private List<Field> daoFields=new ArrayList<Field>();

	@Override
	public void onCreate(Class<T> c) throws Exception {
		loadInjectors(c);
		
		for(Entry<Class<BaseInjector>, BaseInjector> e: injectors.entrySet()){
			e.getValue().onCreate(c);		
			if(e.getValue() instanceof InjectorAttachable){
				attachableInjectors.add((InjectorAttachable) e.getValue());
			}
		}

		log.fine("------------>"+c.getName());
        processMethod(c, c);
		processField(c, c);
                
        Collections.sort(usedInjectors);
	}
	
	private void processMethod(Class c, Class current)throws Exception{
		if(current!=Node.class && current!=AbstractAppState.class && current!=java.lang.Object.class && current!=null)
			processMethod(c, current.getSuperclass());
		
		for(Method m:current.getDeclaredMethods()){
			for(Entry<Class<BaseInjector>, BaseInjector> e: injectors.entrySet()){
				e.getValue().loadMethod(c, m);
				if(e.getValue().hasInjections() && !usedInjectors.contains(e.getValue())){
					usedInjectors.add(e.getValue());
				}
			}
			loadMethod(c, m);
		}
	}
	
	
	private void processField(Class c, Class current)throws Exception{		
		if(current!=Node.class && current!=AbstractAppState.class && current!=java.lang.Object.class && current!=null)
			processField(c, current.getSuperclass());
		
		for(Field f:current.getFields()){
			log.fine("---------------------------------->"+f.getName());
			for(Entry<Class<BaseInjector>, BaseInjector> e: injectors.entrySet()){
				e.getValue().loadField(c, f);
				if(e.getValue().hasInjections() && !usedInjectors.contains(e.getValue())){
					usedInjectors.add(e.getValue());
				}
			}
			loadField(c, f);
		}		
	}

	@Override
	public void onInstance(T item, IBuilder builder) throws Exception {
		item.onPreInject(builder);
		injectInstance(item);
                
		for(Injector i: usedInjectors){
			log.fine("onInstance Injector: "+i);
			i.onInstance(item, builder);
		}
                
        item.onInstance(this);
	}

	public <T extends Injector> T getInjector(Class<T> injector){
		return (T) injectors.get(injector);
	}

	protected void addInjector(Injector i){
		injectors.put((Class<BaseInjector>) i.getClass(), (BaseInjector)i);
	}
	
	@Override
	public boolean hasInjections() {
		return injectors.size()>0;
	}

	@Override
	public void onAttachInstance(T e) {
		try{
			for(InjectorAttachable injector:attachableInjectors){
				injector.onAttach(EntityManager.getGame(), e);
			}
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}

	@Override
	public void onDettachInstance(T e) {
		try{
			for(InjectorAttachable injector:attachableInjectors){
				injector.onDettach(EntityManager.getGame(), e);
			}
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}

    @Override
    public void injectInstance(T e) throws Exception{
        e.setBuilder(this);
    }

	public List<Field> getDaoFields() {
		return daoFields;
	}
	
	
}
