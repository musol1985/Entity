package com.entity.core.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.entity.anot.DAO;
import com.entity.anot.Instance;
import com.entity.anot.OnCollision;
import com.entity.anot.RayPick;
import com.entity.anot.RunOnGLThread;
import com.entity.anot.components.model.SubModelComponent;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.Injector;
import com.entity.core.injectors.TriggerInjector;
import com.entity.core.injectors.field.BodyInjector;
import com.entity.core.injectors.field.CameraInjector;
import com.entity.core.injectors.field.EffectInjector;
import com.entity.core.injectors.field.EntityInjector;
import com.entity.core.injectors.field.LightInjector;
import com.entity.core.injectors.field.ListInjector;
import com.entity.core.injectors.field.MapInjector;
import com.entity.core.injectors.field.MaterialInjector;
import com.entity.core.injectors.field.PersistableInjector;
import com.entity.core.injectors.field.TerrainInjector;
import com.entity.core.injectors.input.InputInjector;
import com.entity.core.injectors.method.UpdateInjector;
import com.entity.core.items.Model;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ModelBuilder extends Builder<Model>{
	public static final String ENTITY_MODEL_REFERENCE="EntityModelReference";
	public static final String ENTITY_GEOMETRY_REFERENCE="EntityGeometryReference";
	
	private boolean mustEnhance;
	
	private ModelEntity model;

	private List<Field> subModels=new ArrayList<Field>();
	
	private HashMap<Class<Model>, Method> collisions=new HashMap<Class<Model>, Method>();
	
	private List<Field> daoFields=new ArrayList<Field>();

	@Override
	public void loadInjectors(Class<Model> c) throws Exception {
		addInjector(new UpdateInjector<Model>());
		addInjector(new InputInjector<Model>());
		addInjector(new LightInjector<Model>());
		addInjector(new EntityInjector<Model>());
        addInjector(new CameraInjector<Model>());
        addInjector(new TriggerInjector<Model>());
        addInjector(new TerrainInjector<Model>());
        addInjector(new MaterialInjector<Model>());
        addInjector(new EffectInjector<Model>());
        addInjector(new BodyInjector<Model>());
        addInjector(new ListInjector<Model>());
        addInjector(new MapInjector<Model>());
        addInjector(new PersistableInjector<Model>());
		
		model=c.getAnnotation(ModelEntity.class);
                
        Class<? extends Injector>[] customInjectors=EntityManager.getModelCustomInjectors();
		if(customInjectors!=null){
			for(Class<? extends Injector> cInj:customInjectors){
				addInjector(cInj.newInstance());
			}
		}
	}
	
	
	@Override
	public void loadField(Class<Model> cls, Field f) throws Exception {		
		if(EntityManager.isAnnotationPresent(SubModelComponent.class,f)){
			f.setAccessible(true);
			subModels.add(f);
		}else if(EntityManager.isAnnotationPresent(DAO.class,f)){
			f.setAccessible(true);
			daoFields.add(f);
		}
		
	}

	@Override
	public void loadMethod(Class<Model> c, Method m) throws Exception {
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
			collisions.put((Class<Model>) m.getParameterTypes()[0], m);
		}	
	}

    @Override
    public void onInstance(Model e, IBuilder builder, Object[] params) throws Exception {
        injectModel(e);
        super.onInstance(e, builder, params);        		
    }
        
        


	@Override
	public boolean isMustEnhance() {
		return mustEnhance;
	}
	
	private void injectModel(Model e)throws Exception{		
		if(model.name().length()>0)
			e.setName(model.name());
		
		if(!model.asset().isEmpty()){
			Node n=(Node) EntityManager.getAssetManager().loadModel(model.asset());
			if(model.smartReference())
				n.setUserData(ENTITY_MODEL_REFERENCE, e);
			
			for(Field f:subModels){
				SubModelComponent a=EntityManager.getAnnotation(SubModelComponent.class,f);
				Spatial s=n.getChild(a.name());
				if(a.rayPickResponse() && s instanceof Geometry){
					s.setUserData(ENTITY_GEOMETRY_REFERENCE, e);
				}
				
				f.set(e, s);
			}
			
			e.attachChild(n);
		}
	}

	
	public Method collidesWith(Model e){
		return collisions.get(e.getClass());
	}



}
