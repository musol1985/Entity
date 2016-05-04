package com.entity.core.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.entity.anot.Cache;
import com.entity.bean.MaterialBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.BaseInjector;
import com.entity.core.interceptors.BaseMethodInterceptor;
import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Node;

public abstract class Builder<T extends IEntity> implements IBuilder<T>{
	private static final Logger log = Logger.getLogger(Builder.class.getName());
	
	private HashMap<Class<BaseInjector>, BaseInjector> injectors=new HashMap<Class<BaseInjector>, BaseInjector>();
	private List<BaseInjector> usedInjectors=new ArrayList<BaseInjector>();
	private List<InjectorAttachable> attachableInjectors=new ArrayList<InjectorAttachable>();
	
	private List<Field> daoFields=new ArrayList<Field>();
	private BaseMethodInterceptor interceptor;
	
	protected boolean cache;

	protected abstract void initBuilder(Class<T> c)throws Exception;
	
	@Override
	public void onCreate(Class<T> c) throws Exception {
		initBuilder(c);
		loadInjectors(c);
		
		for(Entry<Class<BaseInjector>, BaseInjector> e: injectors.entrySet()){
			e.getValue().onCreate(c);		
			if(e.getValue() instanceof InjectorAttachable){
				attachableInjectors.add((InjectorAttachable) e.getValue());
			}
		}
		
		cache=c.isAnnotationPresent(Cache.class);		

		log.info("------------>"+c.getName());
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
		
		for(Field f:current.getDeclaredFields()){
			log.info("---------------------------------->"+f.getName());
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
	public void onInstance(T item, IBuilder builder, Object[] params) throws Exception {
		item.onPreInject(builder, params);
		injectInstance(item);
                
		for(Injector i: usedInjectors){
			log.info("onInstance Injector: "+i);
			i.onInstance(item, builder, params);
		}
                
        item.onInstance(this, params);
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

	public BaseMethodInterceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(BaseMethodInterceptor interceptor) {
		this.interceptor = interceptor;
	}
	
	public boolean isCache(){
		return cache;
	}
}
