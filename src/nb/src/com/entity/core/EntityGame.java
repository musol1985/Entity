package com.entity.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sf.cglib.proxy.MethodProxy;

import org.reflections.Reflections;

import com.entity.anot.CustomInjectors;
import com.entity.anot.Physics;
import com.entity.anot.entities.SceneEntity;
import com.entity.anot.network.NetSync;
import com.entity.anot.network.Network;
import com.entity.bean.AnnotationFieldBean;
import com.entity.bean.MethodSceneBean;
import com.entity.core.interceptors.ClickInterceptor;
import com.entity.core.items.Scene;
import com.entity.network.core.NetGame;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;

public abstract class EntityGame extends SimpleApplication{
	private FilterPostProcessor postProcessor;
	private List<Filter> filters;
	private ClickInterceptor clickInterceptor;
	private BulletAppState bullet;
	
	private HashMap<Class, HashMap<Type, AnnotationFieldBean<NetSync>>> netSyncFields=new HashMap<Class, HashMap<Type, AnnotationFieldBean<NetSync>>>();
	private HashMap<String, MethodSceneBean> scenes=new HashMap<String, MethodSceneBean>();
	
	private String path;
	
	protected NetGame net;
	
	@Override
	public void simpleInitApp() {
		try{						 			
			Physics physics=getClass().getSuperclass().getAnnotation(Physics.class);
			if(physics!=null){
				bullet = new BulletAppState();
				if(physics.debug())
					bullet.setDebugEnabled(true);
				getStateManager().attach(bullet);
			}
			
			Network network=getClass().getSuperclass().getAnnotation(Network.class);
			if(network!=null){
				for(String pack:network.messagesPackage()){
					 Reflections reflections = new Reflections(pack);
		
					 Set<Class<?>> messages=reflections.getTypesAnnotatedWith(Serializable.class);
					 
					 for(Class<?> message:messages){
						 Serializer.registerClass(message);
					 }
				}
				net=new NetGame(network);
			}
			
			Method firstScene=null;
			MethodSceneBean firstBean=null;
			for(Method m:getClass().getSuperclass().getDeclaredMethods()){
				if(m.isAnnotationPresent(SceneEntity.class)){
					SceneEntity anot=m.getAnnotation(SceneEntity.class);
					Scene scene=null;
					if(anot.preLoad()){
						scene=(Scene) EntityManager.instanceGeneric(m.getParameterTypes()[0]);
					}
					MethodSceneBean bean=new MethodSceneBean(scene, anot);
					
					scenes.put(m.getName(), bean);
					if(anot.first()){						
						firstScene=getClass().getMethod(m.getName(), m.getParameterTypes());
						firstBean=bean;						
					}
				}
			}
                        
            CustomInjectors customs=getClass().getAnnotation(CustomInjectors.class);
			if(customs!=null){
				EntityManager.setCustomInjectors(customs.sceneInjectors(), customs.modelInjectors());
			}
                        
			if(firstScene!=null){
				firstScene.invoke(this, firstBean.getS());
			}
				//setScene((Scene) firstScene.invoke(this, null));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addPostProcessor(Filter f)throws Exception{
		if(postProcessor==null){
			postProcessor=new FilterPostProcessor(EntityManager.getAssetManager());
			filters=new ArrayList<Filter>();
			viewPort.addProcessor(postProcessor);
		}
		postProcessor.removeAllFilters();
		filters.add(f);
		Collections.sort(filters, new Comparator<Filter>() {
			@Override
			public int compare(Filter o1, Filter o2) {
				return 0;
			}
		});
		
		for(Filter filter:filters){
			postProcessor.addFilter(filter);
		}
	}
	
	public void removePostProcessor(Filter f)throws Exception{
		postProcessor.removeFilter(f);
		filters.remove(f);
	}
	

	
	public void setScene(Scene scene){
		try {
			Scene current=getStateManager().getState(Scene.class);
			if(current!=null)
				current.dettach();
			
			scene.attachToParent(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setClickInterceptor(ClickInterceptor clickInterceptor) {
		this.clickInterceptor = clickInterceptor;
	}

	public ClickInterceptor getClickInterceptor() {
		return clickInterceptor;
	}
	
	public void addSyncFields(Class c, HashMap<Type, AnnotationFieldBean<NetSync>> beans){
		netSyncFields.put(c, beans);
	}

	public HashMap<Type, AnnotationFieldBean<NetSync>> getSyncFieldsByClass(Class c){
		return netSyncFields.get(c);
	}
	
	public PhysicsSpace getPhysics(){
		return bullet.getPhysicsSpace();
	}

	
	protected Scene showScene(Method m, MethodProxy proxy)throws Exception, Throwable{
		MethodSceneBean bean=scenes.get(m.getName());

		if(!bean.getAnot().singleton() || bean.getS()==null){
			Scene scene=(Scene) EntityManager.instanceGeneric(m.getParameterTypes()[0]);
			bean.setS(scene);
		}
		proxy.invokeSuper(this, new Object[]{bean.getS()});
		//m.invoke(this, new Object[]{bean.getS()});
		
		setScene(bean.getS());

		return bean.getS();
	}
	
	public String getPath(){
		return path;
	}
	
	public void setPath(String path){
		this.path=path;
	}

	public NetGame getNet() {
		if(net==null)
			throw new RuntimeException("No network started");
		return net;
	}
	
	
}
