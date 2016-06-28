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
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

import org.reflections.Reflections;

import com.entity.adapters.TaskAdapter;
import com.entity.anot.CustomInjectors;
import com.entity.anot.Executor;
import com.entity.anot.Physics;
import com.entity.anot.Task;
import com.entity.anot.entities.SceneEntity;
import com.entity.anot.network.NetSync;
import com.entity.anot.network.Network;
import com.entity.bean.AnnotationFieldBean;
import com.entity.bean.FieldSceneBean;
import com.entity.core.interceptors.ClickInterceptor;
import com.entity.core.items.Scene;
import com.entity.modules.gui.GUIGame;
import com.entity.modules.gui.anot.GUI;
import com.entity.network.core.NetGame;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.effect.ParticleEmitter;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;

public abstract class EntityGame extends SimpleApplication{
	protected static final Logger log = Logger.getLogger(EntityGame.class.getName());
	
	private FilterPostProcessor postProcessor;
	private List<Filter> filters;
	private ClickInterceptor clickInterceptor;
	private BulletAppState bullet;
	
	private HashMap<Class, HashMap<Type, AnnotationFieldBean<NetSync>>> netSyncFields=new HashMap<Class, HashMap<Type, AnnotationFieldBean<NetSync>>>();
	private HashMap<Class, Stack> cache=new HashMap<Class, Stack>();
	private HashMap<String, Stack<ParticleEmitter>> particlesCache=new HashMap<String, Stack<ParticleEmitter>>();
	
	private ScheduledExecutorService tasks = Executors.newScheduledThreadPool(1);          
	
	private String path;
	
	protected NetGame net;
	protected GUIGame gui;
	
	private ScheduledThreadPoolExecutor executor;
	
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
				bullet.setEnabled(physics.active());
				
				getStateManager().attach(bullet);
			}
			
			GUI anotGUI=getClass().getAnnotation(GUI.class);
			if(anotGUI!=null){
				gui=new GUIGame(anotGUI);
			}
			
			Network network=getClass().getAnnotation(Network.class);
			if(network!=null){
				List<String> packages=new ArrayList<String>(Arrays.asList(network.messagesPackage()));
				
				packages.add("com.entity.network.core.msg");
				packages.add("com.entity.network.core.dao");
                packages.add("com.entity.network.core.beans");
                packages.add("com.entity.utils");
				
				log.info("++Finding @Serializable in "+packages.size()+" packages");
				List<Class> classes=new ArrayList<Class>();
				
				for(String pack:packages){
					 Reflections reflections = new Reflections(pack);
		
					 Set<Class<?>> messages=reflections.getTypesAnnotatedWith(Serializable.class);
					 log.info("++Found "+messages.size()+" @Serializable in package "+pack);
					 
					 classes.addAll(messages);
					 					 
				}
				Collections.sort(classes, new Comparator<Class>(){
					@Override
					public int compare(Class arg0, Class arg1) {
						return arg0.getName().compareTo(arg1.getName());
					}
					
				});
				
				log.info("++++++Registering "+classes.size()+" classes");
				for(Class<?> message:classes){
					 log.info("++++++Registering network class "+message.getName());
					 Serializer.registerClass(message);
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
			
			Executor exe=getClass().getAnnotation(Executor.class);
			if(exe!=null){
				executor=new ScheduledThreadPoolExecutor(exe.threads());
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

		postProcessor.addFilter(f);
	}
	
	public void removePostProcessor(Filter f)throws Exception{
		postProcessor.removeFilter(f);
	}
	
	public void addProcessor(SceneProcessor p)throws Exception{
		viewPort.addProcessor(p);
	}
	
	public void removeProcessor(SceneProcessor p)throws Exception{
		viewPort.removeProcessor(p);
	}
	
        public void onAddShadowRender(){
            if(postProcessor!=null){
               viewPort.removeProcessor(postProcessor);
               viewPort.addProcessor(postProcessor);
            }
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
        
        public boolean isPhysics(){
            return bullet!=null;
        }
        
        public boolean isPhysicsActive(){
            return bullet.isEnabled();
        }
        
        public void activatePhysics(){
            bullet.setEnabled(true);
        }
	
	public PhysicsSpace getPhysics(){
		return bullet.getPhysicsSpace();
	}

	public <T extends Scene> T showScene(T scene){
		return showScene(scene, null);
	}
	
	public <T extends Scene> T showScene(T scene, Object...params){
            try{
		if(!scene.isPreLoaded()){
			FieldSceneBean bean=scene.getProxy();
			scene=(T) EntityManager.instanceGeneric(bean.getF().getType());
			
			bean.getF().set(bean.getG(), scene);
			
			if(bean.getAnot().singleton())
				scene.setProxy(bean);
		}
		if(params!=null && params.length>0){
			log.info("Scene set params: "+params.length);
			scene.setParams(params);
		}
		
		setScene(scene);
            }catch(Exception e){
                log.severe("Can't load scene "+scene);
                e.printStackTrace();                
            }
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
	
	public GUIGame getGUI() {
		if(gui==null)
			throw new RuntimeException("No gui started");
		return gui;
	}
	
	public String getPersistPath(){
		return "/EntityPersist";
	}

	@Override
	public void destroy() {
		try{
			if(net!=null){
				if(getNet().isNetServerGame()){
					net.getServer().close();
				}else{
					net.getClient().close();
				}
			}
		}catch(Exception e){
			log.info("Error closing network connection: "+e.getMessage());
		}
		super.destroy();
	}

	public ScheduledThreadPoolExecutor getExecutor() {
		if(executor==null)
			throw new RuntimeException("Trying to get a null executor. Please put @Executor in your EntityGame class");
		return executor;
	}
	
	public synchronized <T> T getFromCache(Class<T> c){
		Stack subcache=cache.get(c);
		if(subcache!=null){
			return (T)subcache.pop();
		}
		return null;
	}
	
	public synchronized <T> void putInCache(T c){
		Stack subcache=cache.get(c);
		if(subcache==null){
			subcache=new Stack();
			cache.put(c.getClass(), subcache);
		}
		subcache.push(c);		
	}

	public HashMap<String, Stack<ParticleEmitter>> getParticlesCache() {
		return particlesCache;
	}
	
	public void addTask(TaskAdapter task, Task t){
		tasks.scheduleAtFixedRate(task, t.delay(), t.period(), t.unit());
	}
	
	public void removeTasks(){
		tasks.shutdownNow();
	}
	
}
