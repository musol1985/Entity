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
import com.entity.anot.RunGLThread;
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
import com.entity.core.injectors.field.TerrainInjector;
import com.entity.core.injectors.input.InputInjector;
import com.entity.core.injectors.method.UpdateInjector;
import com.entity.core.items.Model;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ModelBuilder extends BaseModelBuilder<Model>{
	private ModelEntity model;
	private List<Field> subModels=new ArrayList<Field>();
	
	@Override
	public void loadInjectors(Class<Model> c) throws Exception {
		super.loadInjectors(c);
		
		model=c.getAnnotation(ModelEntity.class);
	}
	
	
	
    @Override
	public void loadField(Class<Model> cls, Field f) throws Exception {
    	if(f.isAnnotationPresent(SubModelComponent.class)){
			f.setAccessible(true);
			subModels.add(f);
		}else{
			super.loadField(cls, f);
		}
	}



	@Override
    public void onInstance(Model e, IBuilder builder) throws Exception {
        injectModel(e);
        super.onInstance(e, builder);        		
    }
    
    private void injectModel(Model e)throws Exception{		
		if(model.name().length()>0)
			e.setName(model.name());
		
		if(!model.asset().isEmpty()){
			Node n=(Node) EntityManager.getAssetManager().loadModel(model.asset());
			if(model.smartReference())
				n.setUserData(ENTITY_MODEL_REFERENCE, e);
			
			for(Field f:subModels){
				SubModelComponent a=f.getAnnotation(SubModelComponent.class);
				Spatial s=n.getChild(a.name());
				if(a.rayPickResponse() && s instanceof Geometry){
					s.setUserData(ENTITY_GEOMETRY_REFERENCE, e);
				}
				
				f.set(e, s);
			}
			
			e.attachChild(n);
		}
	}
	
}
