package com.entity.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodProxy;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Instance;
import com.entity.anot.RayPick;
import com.entity.core.interceptors.BaseMethodInterceptor;
import com.entity.core.interceptors.RayPickInterceptor;
import com.entity.core.items.Scene;
import com.google.dexmaker.stock.ProxyBuilder;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public abstract class EntityManager {
	protected static final Logger log = Logger.getLogger(EntityManager.class.getName());
	
	private static HashMap<Class, IBuilder> builders=new HashMap<Class, IBuilder>(); 
	private static Scene<? extends EntityGame> currentScene;
	private static EntityGame game;
        
    private static Class[] sceneCustomInjectors;
	private static Class[] modelCustomInjectors;
	
	public static <T extends EntityGame> T startGame(Class<T> gameClass)throws Exception{
		return startGame(gameClass, true);
	}
	
	public static <T extends EntityGame> T startGame(Class<T> gameClass, boolean autostart)throws Exception{
		game=gameClass.newInstance();
		game.setPauseOnLostFocus(false);
		game.start();
		return (T) game;
	}
	
	public static IBuilder getBuilder(Class entityClass)throws Exception{
		IBuilder builder=builders.get(entityClass);
		if(builder==null){
			builder=build(getBuildClassFromClass(entityClass), entityClass);
			builders.put(entityClass, builder);
		}
		
		return builder;
	}
	
	private static Class getBuildClassFromClass(Class cls)throws Exception{
		BuilderDefinition anot=(BuilderDefinition) cls.getAnnotation(BuilderDefinition.class);
		if(anot==null)
			throw new Exception("No builder definition asigned to "+cls.getName());
		
		return anot.builderClass();
	}
	
	private static IBuilder build(Class<IBuilder> builderClass, Class entityClass)throws Exception{
		IBuilder builder=builderClass.newInstance();
		log.fine("Builder "+builder.getClass().getName()+" pre created for entity "+entityClass.getName());
		builder.onCreate(entityClass);
		log.fine("Builder "+builder.getClass().getName()+" created for entity "+entityClass.getName());
		return builder;
	}

	public static Scene<? extends EntityGame> getCurrentScene() {
		return currentScene;
	}

	public static void setCurrentScene(Scene<? extends EntityGame> currentScene) {
		EntityManager.currentScene = currentScene;
	}
	
	public static AssetManager getAssetManager(){
		return game.getAssetManager();
	}
	
	public static Camera getCamera(){
		return game.getCamera();
	}
        
        public static EntityGame getGame(){
            return game;
        }
        
        public static <T extends EntityGame> void setGame(T game){
            EntityManager.game=game;
        }
	
	public static Node getRootNode(){
		return game.getRootNode();
	}
	
	public static InputManager getInputManager(){
		return game.getInputManager();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IEntity> T instance(Class<T> c){
		return (T) instanceGeneric(c);
	}
	
	public static <T extends Injector> T getInjector(Class cls, Class<T> injectorClass)throws Exception{
            cls=getClass(cls);
            return (T) getBuilder(cls).getInjector(injectorClass);
	}
        
    public static Class getClass(Class cls)throws Exception{
        /*if(cls.getName().contains("CGLIB"))
            return Class.forName((cls).getGenericSuperclass().getTypeName());*/
        return cls;
    }
	
	@SuppressWarnings("unchecked")
	public static Object instanceGeneric(Class c){
		IEntity res=null;
		try{
			IBuilder template=EntityManager.getBuilder(c);
			if(template.isMustEnhance()){
				if(EntityManager.isAndroidGame()){
					res=(IEntity)ProxyBuilder.forClass(c).handler(interceptor).build();
				}else{
					res=(IEntity)Enhancer.create(c, interceptor);
				}
			}else{
				res=(IEntity) c.newInstance();
			}
			log.fine("onInstance "+res.getClass().getName()+" using builder "+template.getClass().getName());
			template.onInstance(res, template);
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	private static BaseMethodInterceptor interceptor=new BaseMethodInterceptor() {

		@Override
		public Object interceptMethod(Object obj, Method method, MethodProxy mp, Object[] args) throws Throwable {
			if(EntityManager.isAnnotationPresent(RayPick.class,method)){
				return RayPickInterceptor.rayPick(obj, method, args, mp, this);
			}else if(EntityManager.isAnnotationPresent(Instance.class,method)){
                IEntity instance=(IEntity)instanceGeneric(method.getParameterTypes()[0]);
                Instance anot=EntityManager.getAnnotation(Instance.class,method);
                if(Instance.THIS.equals(anot.attachTo())){
                    IEntity e=(IEntity)obj;
                    instance.attachToParent(e);
                }else{
                    IEntity e=(IEntity)obj.getClass().getField(anot.attachTo()).get(obj);
                    instance.attachToParent(e);
                }
                return callSuper(obj, method, mp, new Object[]{instance});
            }else{
                return callSuper(obj, method, mp,  args);
			}
		}
	};

	public static void setCustomInjectors(Class[] sceneInjectors, Class[] modelInjectos){
		EntityManager.sceneCustomInjectors=sceneInjectors;
		EntityManager.modelCustomInjectors=modelInjectos;
	}

	public static Class[] getSceneCustomInjectors() {
		return sceneCustomInjectors;
	}

	public static Class[] getModelCustomInjectors() {
		return modelCustomInjectors;
	}

    
    public static <T> T loadPersistable(String file){
        T obj=null;
        file=getGame().getPath()+"/"+file;
        if(new File(file).exists()){
            try{
               FileInputStream fileIn = new FileInputStream(file);
               GZIPInputStream gz = new GZIPInputStream(fileIn);
               ObjectInputStream in = new ObjectInputStream(gz);
               obj = (T) in.readObject();
               in.close();
               fileIn.close();
               log.fine("Load persistable file :"+file);
            }catch(Exception i){
            	log.warning("Exception on loading persistable file "+file+" :"+ i.getMessage());
            	i.printStackTrace();
            }
        }
        return obj;
    }
    
    public static boolean savePersistable(String file, Object obj){
    	try{
            FileOutputStream fileOut = new FileOutputStream(file);
            GZIPOutputStream gz = new GZIPOutputStream(fileOut);
            ObjectOutputStream out = new ObjectOutputStream(gz);
            out.writeObject(obj);
            out.close();
            gz.close();
            fileOut.close();
            log.fine("Serialized data "+file);
            return true;
         }catch(IOException i){
        	 log.warning("Exception on saving persistable file "+file+" :"+ i.getMessage());
             i.printStackTrace();
         }
    	return false;
    }
    
    public static boolean isAndroidGame(){
    	return game instanceof AndroidGame;
    }
    
    public static boolean isAnnotationPresent(Class<? extends Annotation> a, Field f){
    	return getAnnotation(a, f)!=null;
    }
    
    public  static <T extends Annotation> T getAnnotation(Class<T> a, Field f){
    	T res=f.getAnnotation(a);
    	if(res!=null)
    		return res;

		try {
			Field fSuper = f.getDeclaringClass().getSuperclass().getField(f.getName());
			if(fSuper!=null){
	    		return getAnnotation(a, fSuper);
	    	}
		} catch (NoSuchFieldException e) {
			
		}
    	
    	return null;
    }
    
    public static boolean isAnnotationPresent(Class<? extends Annotation> a, Method m){
    	return getAnnotation(a, m)!=null;
    }
    
    public  static <T extends Annotation> T getAnnotation(Class<T> a, Method m){
    	T res=m.getAnnotation(a);
    	if(res!=null)
    		return res;

		try {
			Method fSuper = m.getDeclaringClass().getSuperclass().getMethod(m.getName(), m.getParameterTypes());
			if(fSuper!=null){
	    		return getAnnotation(a, fSuper);
	    	}
		} catch (NoSuchMethodException e) {
			
		}
    	
    	return null;
    }
}	
