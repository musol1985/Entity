package com.entity.core.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.entity.anot.DAO;
import com.entity.anot.Instance;
import com.entity.anot.OnCollision;
import com.entity.anot.RayPick;
import com.entity.anot.RunOnGLThread;
import com.entity.bean.AnnotationMethodBean;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.entity.core.injectors.ListBeanSingletonInjector;
import com.entity.core.injectors.TriggerInjector;
import com.entity.core.injectors.field.BodyInjector;
import com.entity.core.injectors.field.CameraInjector;
import com.entity.core.injectors.field.EntityInjector;
import com.entity.core.injectors.field.LightInjector;
import com.entity.core.injectors.field.ListInjector;
import com.entity.core.injectors.field.MapInjector;
import com.entity.core.injectors.field.MaterialInjector;
import com.entity.core.injectors.field.ParticleInjector;
import com.entity.core.injectors.field.PersistableInjector;
import com.entity.core.injectors.field.PostEffectInjector;
import com.entity.core.injectors.field.ProcessorInjector;
import com.entity.core.injectors.field.SkyInjector;
import com.entity.core.injectors.field.TerrainInjector;
import com.entity.core.injectors.field.VehicleInjector;
import com.entity.core.injectors.input.InputInjector;
import com.entity.core.injectors.method.UpdateInjector;
import com.entity.core.items.Model;
import com.entity.core.items.Scene;

public abstract class BaseModelBuilder<T extends IEntity> extends Builder<T>{
	protected static final Logger log = Logger.getLogger(BaseModelBuilder.class.getName());
	
	public static final String ENTITY_MODEL_REFERENCE="EntityModelReference";
	public static final String ENTITY_GEOMETRY_REFERENCE="EntityGeometryReference";
	
	private boolean mustEnhance;

	
	protected HashMap<Class<Model>, AnnotationMethodBean<OnCollision>> collisions=new HashMap<Class<Model>, AnnotationMethodBean<OnCollision>>();
	protected HashMap<Class, AnnotationMethodBean<OnCollision>> collisionsExt=new HashMap<Class,AnnotationMethodBean<OnCollision>>();
	
	protected List<Field> daoFields=new ArrayList<Field>();

	@Override
	public void loadInjectors(Class<T> c) throws Exception {
		addInjector(new SkyInjector<Scene>());
		addInjector(new UpdateInjector<T>());
		addInjector(new InputInjector<T>());
		addInjector(new LightInjector<T>());
		addInjector(new EntityInjector<T>());
        addInjector(new CameraInjector<T>());
        addInjector(new TriggerInjector<T>());
        addInjector(new TerrainInjector<T>());
        addInjector(new MaterialInjector<T>());
        addInjector(new PostEffectInjector<T>());
        addInjector(new ProcessorInjector<T>());        
        addInjector(new BodyInjector<T>());
        addInjector(new VehicleInjector<T>());
        addInjector(new ListInjector<T>());
        addInjector(new ListBeanSingletonInjector<T>());
        addInjector(new MapInjector<T>());
        addInjector(new PersistableInjector<T>());
        addInjector(new ParticleInjector<T>());
		
        Class<? extends Injector>[] customInjectors=EntityManager.getModelCustomInjectors();
		if(customInjectors!=null){
			for(Class<? extends Injector> cInj:customInjectors){
				addInjector(cInj.newInstance());
			}
		}
	}
	
	
	@Override
	public void loadField(Class<T> cls, Field f) throws Exception {		
		if(EntityManager.isAnnotationPresent(DAO.class,f)){
			f.setAccessible(true);
			daoFields.add(f);
		}		
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {
		if(!mustEnhance){
			if(EntityManager.isAnnotationPresent(RunOnGLThread.class,m)){
				mustEnhance=true;
                        }else if(EntityManager.isAnnotationPresent(Instance.class,m)){
                            mustEnhance=true;
                        }else if(EntityManager.isAnnotationPresent(RayPick.class,m)){
                            mustEnhance=true;
                        }
		}
		if(EntityManager.isAnnotationPresent(OnCollision.class,m)){
			AnnotationMethodBean<OnCollision> bean=new AnnotationMethodBean<OnCollision>(m, OnCollision.class);
			
			if(!bean.getAnnot().includeSubClass()){
				collisions.put((Class<Model>) m.getParameterTypes()[0], bean);
			}else{
				collisionsExt.put(m.getParameterTypes()[0], bean);
			}
		}	
	}


        
        


	@Override
	public boolean isMustEnhance() {
		return mustEnhance;
	}
	
	

	
	public Method collidesWith(Model local, Model remote){		
		AnnotationMethodBean<OnCollision> bean=collisions.get(remote.getClass());
		if(bean!=null){
			return bean.getMethod();
		}		
		for(Entry<Class,AnnotationMethodBean<OnCollision>> beanExt:collisionsExt.entrySet()){
			if(beanExt.getKey().isAssignableFrom(remote.getClass())){
				return beanExt.getValue().getMethod();
			}
		}
		return null;
	}



}
