package com.entity.core.builders;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.components.model.SubModelComponent;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.items.Model;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ModelBuilder<T extends Model> extends BaseModelBuilder<T>{
	public static final String ENTITY_MODEL_REFERENCE="EntityModelReference";
	public static final String ENTITY_GEOMETRY_REFERENCE="EntityGeometryReference";
	
	
	private ModelEntity model;

	private List<Field> subModels=new ArrayList<Field>();


	@Override
	public void loadInjectors(Class<T> c) throws Exception {
		super.loadInjectors(c);
		model=c.getAnnotation(ModelEntity.class);
	}
	
	
	@Override
	public void loadField(Class<T> cls, Field f) throws Exception {	
		super.loadField(cls, f);
		if(EntityManager.isAnnotationPresent(SubModelComponent.class,f)){
			f.setAccessible(true);
			subModels.add(f);
		}
		
	}



    @Override
    public void onInstance(T e, IBuilder builder, Object[] params) throws Exception {
        injectModel(e);
        super.onInstance(e, builder, params);        		
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



}
