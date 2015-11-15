package com.entity.core.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.BaseInjector;
import java.util.Collections;

public abstract class Builder<T extends IEntity> implements IBuilder<T>{
	private HashMap<Class<BaseInjector>, BaseInjector> injectors=new HashMap<Class<BaseInjector>, BaseInjector>();
	private List<BaseInjector> usedInjectors=new ArrayList<BaseInjector>();
	private List<InjectorAttachable> attachableInjectors=new ArrayList<InjectorAttachable>();

	@Override
	public void onCreate(Class<T> c) throws Exception {
		loadInjectors(c);
		
		for(Entry<Class<BaseInjector>, BaseInjector> e: injectors.entrySet()){
			e.getValue().onCreate(c);		
		}

		for(Method m:c.getDeclaredMethods()){
			for(Entry<Class<BaseInjector>, BaseInjector> e: injectors.entrySet()){
				e.getValue().loadMethod(c, m);
				if(e.getValue().hasInjections() && !usedInjectors.contains(e.getValue())){
					usedInjectors.add(e.getValue());
				}
			}
			loadMethod(c, m);
		}
		for(Field f:c.getDeclaredFields()){
			for(Entry<Class<BaseInjector>, BaseInjector> e: injectors.entrySet()){
				e.getValue().loadField(c, f);
				if(e.getValue().hasInjections() && !usedInjectors.contains(e.getValue())){
					usedInjectors.add(e.getValue());
				}
			}
			loadField(c, f);
		}
                
                for(BaseInjector e: usedInjectors){		
			if(e instanceof InjectorAttachable){
				attachableInjectors.add((InjectorAttachable) e);
			}
		}
                
        Collections.sort(usedInjectors);
	}

	@Override
	public void onInstance(T item, IBuilder builder) throws Exception {
		injectInstance(item);
                
		for(Injector i: usedInjectors){
                    System.out.println("onInstance Injector: "+i);
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
	
	
}
