package com.entity.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Instance;
import com.entity.anot.RayPick;
import com.entity.anot.entities.SceneEntity;
import com.entity.core.interceptors.RayPickInterceptor;
import com.entity.core.items.Scene;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public abstract class EntityManager {
	
	private static HashMap<Class, IBuilder> builders=new HashMap<Class, IBuilder>(); 
	private static Scene<? extends EntityGame> currentScene;
	private static EntityGame game;
        
    private static Class[] sceneCustomInjectors;
	private static Class[] modelCustomInjectors;
	
	public static <T extends EntityGame> T startGame(Class<T> gameClass)throws Exception{
		return startGame(gameClass, true);
	}
	
	public static <T extends EntityGame> T startGame(Class<T> gameClass, boolean autostart)throws Exception{
		game=(EntityGame) Enhancer.create(gameClass, gameInterceptor);
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
		System.out.println("Builder "+builder.getClass().getName()+" pre created for entity "+entityClass.getName());
		builder.onCreate(entityClass);
		System.out.println("Builder "+builder.getClass().getName()+" created for entity "+entityClass.getName());
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
            if(cls.getName().contains("CGLIB"))
                return Class.forName((cls).getGenericSuperclass().getTypeName());
            return cls;
        }
	
	@SuppressWarnings("unchecked")
	public static Object instanceGeneric(Class c){
		IEntity res=null;
		try{
			IBuilder template=EntityManager.getBuilder(c);
			if(template.isMustEnhance()){
				res=(IEntity)Enhancer.create(c, interceptor);
			}else{
				res=(IEntity) c.newInstance();
			}
			System.out.println("onInstance "+res.getClass().getName()+" using builder "+template.getClass().getName());
			template.onInstance(res, template);
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	private static MethodInterceptor interceptor=new MethodInterceptor() {
		
		public Object intercept(Object obj,  Method method,  Object[] args,  MethodProxy proxy)
				throws Throwable {

			if(method.isAnnotationPresent(RayPick.class)){
				return RayPickInterceptor.rayPick(obj, method, args, proxy);
			}else if(method.isAnnotationPresent(Instance.class)){
                            IEntity instance=(IEntity)instanceGeneric(method.getParameterTypes()[0]);
                            Instance anot=method.getAnnotation(Instance.class);
                            if("this".equals(anot.attachTo())){
                                IEntity e=(IEntity)obj;
                                instance.attachToParent(e);
                            }else{
                                IEntity e=(IEntity)obj.getClass().getField(anot.attachTo()).get(obj);
                                instance.attachToParent(e);
                            }
                            return proxy.invokeSuper(obj, new Object[]{instance});
                        }else{
                            return proxy.invokeSuper(obj, args);
			}
		}
	};
	
	private static MethodInterceptor gameInterceptor=new MethodInterceptor() {
		public Object intercept(Object obj,  Method method,  Object[] args,  MethodProxy proxy)
				throws Throwable {
			if(method.isAnnotationPresent(SceneEntity.class)){
				return game.showScene(method, proxy);
			}else{
				return proxy.invokeSuper(obj, args);
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

    /*public void save(Object obj){
        if(changed){
            changed=false;
            try{
                FileOutputStream fileOut = new FileOutputStream(getFileName());
                GZIPOutputStream gz = new GZIPOutputStream(fileOut);
                ObjectOutputStream out = new ObjectOutputStream(gz);
                out.writeObject(this);
                out.close();
                gz.close();
                fileOut.close();
                System.out.println("Serialized data "+getFileName());
                if(this instanceof CeldaDAO){
                    System.out.println("Estaticos de la celda: "+id+" "+((CeldaDAO)this).estaticos.size());
                }
             }catch(IOException i){
                 i.printStackTrace();
             }
        }
    }*/
    
    
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
            }catch(Exception i){
               i.printStackTrace();
            }
        }
        return obj;
    }
}	
