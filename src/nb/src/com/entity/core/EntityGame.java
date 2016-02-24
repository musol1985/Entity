package com.entity.core;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.reflections.Reflections;

import com.entity.anot.CamNode;
import com.entity.anot.CustomInjectors;
import com.entity.anot.Physics;
import com.entity.anot.entities.SceneEntity;
import com.entity.anot.network.NetSync;
import com.entity.anot.network.Network;
import com.entity.bean.AnnotationFieldBean;
import com.entity.bean.FieldSceneBean;
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
	protected static final Logger log = Logger.getLogger(EntityGame.class.getName());
	
	private FilterPostProcessor postProcessor;
	private List<Filter> filters;
	private ClickInterceptor clickInterceptor;
	private BulletAppState bullet;
	
	private HashMap<Class, HashMap<Type, AnnotationFieldBean<NetSync>>> netSyncFields=new HashMap<Class, HashMap<Type, AnnotationFieldBean<NetSync>>>();
	
	private String path;
	
	protected NetGame net;
	
	@Override
	public void simpleInitApp() {
                if(EntityManager.getGame()==null)
                    EntityManager.setGame(this);
		try{				
			path=getPersistPath();
			
			Physics physics=getClass().getAnnotation(Physics.class);
			if(physics!=null){
				bullet = new BulletAppState();
				if(physics.debug())
					bullet.setDebugEnabled(true);
				getStateManager().attach(bullet);
			}
			
			Network network=getClass().getAnnotation(Network.class);
			if(network!=null){
				List<String> packages=Arrays.asList(network.messagesPackage());
				
				packages.add("com.entity.network.core.msg");
				packages.add("com.entity.network.core.bean");
				
				for(String pack:packages){
					 Reflections reflections = new Reflections(pack);
		
					 Set<Class<?>> messages=reflections.getTypesAnnotatedWith(Serializable.class);
					 
					 for(Class<?> message:messages){
						 Serializer.registerClass(message);
					 }
				}
				net=new NetGame(network);
			}
			
			Scene firstScene=null;
			for(Field f:getClass().getDeclaredFields()){
				if(EntityManager.isAnnotationPresent(SceneEntity.class,f)){
					SceneEntity anot=EntityManager.getAnnotation(SceneEntity.class,f);
					
					Scene scene=null;
					if(anot.preLoad() || anot.first()){
						scene=(Scene) EntityManager.instanceGeneric(f.getType());
						f.set(this, scene);
						if(anot.first())
							firstScene=scene;
						if(anot.singleton())
							scene.setProxy(new FieldSceneBean(f, this, anot));
					}else{
						FieldSceneBean fsb=new FieldSceneBean(f, this, anot);
						scene=(Scene) f.getType().newInstance();
						scene.setProxy(fsb);
						f.set(this, scene);
					}					
				}
			}
		
                        
            CustomInjectors customs=getClass().getAnnotation(CustomInjectors.class);
			if(customs!=null){
				EntityManager.setCustomInjectors(customs.sceneInjectors(), customs.modelInjectors());
			}
			
			if(firstScene!=null){
				setScene(firstScene);
			}else{
				log.warning("No first scene defined. You must call setScene(@SceneObject) manually");
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
	

	
	private void setScene(Scene scene){
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

	
	public <T extends Scene> T showScene(T scene, Object...params)throws Exception{
		if(!scene.isPreLoaded()){
			FieldSceneBean bean=scene.getProxy();
			scene=(T) EntityManager.instanceGeneric(bean.getF().getType());
			
			bean.getF().set(bean.getG(), scene);
			
			if(bean.getAnot().singleton())
				scene.setProxy(bean);
		}
		if(params!=null && params.length>0){
			log.fine("Scene set params: "+params.length);
			scene.setParams(params);
		}
		
		setScene(scene);

		return scene;
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
	
	public String getPersistPath(){
		return "/EntityPersist";
	}
	
}
