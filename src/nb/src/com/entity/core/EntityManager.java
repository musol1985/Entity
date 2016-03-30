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

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Persistable;
import com.entity.core.interceptors.BaseMethodInterceptor;
import com.entity.core.items.Scene;
import com.google.dexmaker.stock.ProxyBuilder;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.JmeContext;

public abstract class EntityManager {
	protected static final Logger log = Logger.getLogger(EntityManager.class.getName());
	
	private static HashMap<Class, IBuilder> builders=new HashMap<Class, IBuilder>(); 
	private static Scene<? extends EntityGame> currentScene;
	private static EntityGame game;
        
    private static Class[] sceneCustomInjectors;
	private static Class[] modelCustomInjectors;
	
	public static <T extends EntityGame> T startGame(Class<T> gameClass)throws Exception{
		return startGame(gameClass, true, false);
	}
	
	public static <T extends EntityGame> T startGame(Class<T> gameClass, boolean autostart, boolean server)throws Exception{
		game=gameClass.newInstance();
		game.setPauseOnLostFocus(false);
		if(server){
			game.start(JmeContext.Type.Headless);
		}else{
			game.start();
		}
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
		log.info("Builder "+builder.getClass().getName()+" pre created for entity "+entityClass.getName());
		builder.onCreate(entityClass);
		log.info("Builder "+builder.getClass().getName()+" created for entity "+entityClass.getName());
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
    
    public static Object instanceGeneric(Class c){
    	return instanceGeneric(c, null);
    }
	
	@SuppressWarnings("unchecked")
	public static Object instanceGeneric(Class c, Object...params){
		IEntity res=null;
		try{
			IBuilder template=EntityManager.getBuilder(c);
			if(template.isCache())
				res=(IEntity)game.getFromCache(c);
			if(res==null){
				if(template.isMustEnhance()){				
					System.out.println("Enhance ####################################-------------------------------------------------------------------------------->"+c.getName());
					if(template.getInterceptor()==null){
						BuilderDefinition anot=(BuilderDefinition) c.getAnnotation(BuilderDefinition.class);
						template.setInterceptor((BaseMethodInterceptor) anot.methodInterceptorClass().newInstance());
					}
					if(EntityManager.isAndroidGame()){
						res=(IEntity)ProxyBuilder.forClass(c).handler(template.getInterceptor()).build();
					}else{
						res=(IEntity)Enhancer.create(c, template.getInterceptor());
					}
					if(res instanceof Node || res instanceof Scene)
						throw new RuntimeException("You can't use enhancer annotations in Node or Scene class!!! "+res.getClass().getName());
				}else{
					res=(IEntity) c.newInstance();
				}				
				log.info("onInstance "+res.getClass().getName()+" using builder "+template.getClass().getName());
				template.onInstance(res, template, params);
			}else{
				log.info("OnInstanceCache "+res.getClass().getName()+" using builder "+template.getClass().getName());
				res.onInstanceCache(template, params);
			}			
		}catch(Exception e){
            log.severe("Can't instantiate an entity of class "+c.getName()+" reason: "+e.getMessage());
                    e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return res;
	}

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
               log.info("Load persistable file :"+file);
            }catch(Exception i){
            	log.warning("Exception on loading persistable file "+file+" :"+ i.getMessage());
            	i.printStackTrace();
            }
        }
        return obj;
    }
    
    public static boolean savePersistable(String file, Object obj){
    	try{
            File f=new File(getGame().getPath()+"/"+file);
            if(!f.getParentFile().exists())
                f.getParentFile().mkdirs();
            FileOutputStream fileOut = new FileOutputStream(getGame().getPath()+"/"+file);
            GZIPOutputStream gz = new GZIPOutputStream(fileOut);
            ObjectOutputStream out = new ObjectOutputStream(gz);
            out.writeObject(obj);
            out.close();
            gz.close();
            fileOut.close();
            log.info("Serialized data "+file);
            return true;
         }catch(IOException i){
        	 log.warning("Exception on saving persistable file "+file+" :"+ i.getMessage());
             i.printStackTrace();
         }
    	return false;
    }
    
    public static boolean savePersistableFieldName(Object obj, String field){
    	try{
    		Field f=obj.getClass().getField(field);
                f.setAccessible(true);
    		Persistable anot=getAnnotation(Persistable.class, f);
    		if(anot!=null){
    			return savePersistable(anot.fileName(), f.get(obj));
    		}else{
    			throw new Exception("No @Persistable annotation found");
    		}
    	}catch(Exception e){
    		log.warning("Exception on savePersistableFieldName "+obj+":"+field+" "+e.getMessage());
            e.printStackTrace();
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
                        Class cls=m.getDeclaringClass().getSuperclass();
                        if(cls!=null){
                            Method fSuper = cls.getMethod(m.getName(), m.getParameterTypes());
                            if(fSuper!=null){
                            return getAnnotation(a, fSuper);
                        }
	    	}
		} catch (NoSuchMethodException e) {
			
		}
    	
    	return null;
    }
}	
