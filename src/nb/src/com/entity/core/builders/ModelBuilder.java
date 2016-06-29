package com.entity.core.builders;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.entity.anot.components.model.OnlyPosition;
import com.entity.anot.components.model.SubModelComponent;
import com.entity.anot.components.model.SubModelMapComponent;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.items.Model;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ModelBuilder<T extends Model> extends BaseModelBuilder<T>{
	public static final String COLLISION_FILTER_ID="CollisionFilterID";
	public static final String ENTITY_MODEL_REFERENCE="EntityModelReference";
	public static final String ENTITY_GEOMETRY_REFERENCE="EntityGeometryReference";
	
	
	private ModelEntity model;

	private List<Field> subModels=new ArrayList<Field>();


	@Override
	public void loadInjectors(Class<T> c) throws Exception {
		super.loadInjectors(c);
	}
	
	
	@Override
	public void loadField(Class<T> cls, Field f) throws Exception {	
		super.loadField(cls, f);
		if(EntityManager.isAnnotationPresent(SubModelComponent.class,f) || EntityManager.isAnnotationPresent(SubModelMapComponent.class,f)){
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
				if(a!=null){
					Spatial s=n.getChild(a.name());
					if(s!=null){
						OnlyPosition opA=EntityManager.getAnnotation(OnlyPosition.class,f);
						if(opA!=null){
							Vector3f pos=null;
							if(opA.positionVector()==OnlyPosition.TYPE_POSITION.LOCAL){
								pos=s.getLocalTranslation().clone();
							}else{
								pos=s.getWorldTranslation().clone();
							}
							f.set(e, pos);
						}else{
							if(a.rayPickResponse() && s instanceof Geometry){
								s.setUserData(ENTITY_GEOMETRY_REFERENCE, e);
							}

							f.set(e, s);
						}
						if(a.dettach() && s.getParent()!=null)
							s.getParent().detachChild(s);
					}else{
						log.warning("No submodel with name: "+a.name());
					}
				}else{
					SubModelMapComponent anot=EntityManager.getAnnotation(SubModelMapComponent.class,f);
					if(anot!=null){
						HashMap<String, Object> map=new HashMap<String, Object>();
						getChildMap(map, n, n, anot.nameStartsWith(), anot.rayPickResponse());
							
						f.set(e, map);
					}
				}
			}
			
			e.attachChild(n);
		}
	}

	public void getChildMap(List<Spatial> res, Node parent,  Node n, String name, boolean raypick) {
		for (Spatial child : n.getChildren()) {
			if(child instanceof Node){
				getChildMap(res, parent, (Node)child, name, raypick);
			}
			if (child.getName()!=null && child.getName().startsWith(name)){
				if(raypick && child instanceof Geometry){
					child.setUserData(ENTITY_GEOMETRY_REFERENCE, parent);
				}
				res.add(child);
			}
		}
	}

	public void getChildMap(HashMap<String, Object> res, Node parent, Node n, String name, boolean raypick) {
		for (Spatial child : n.getChildren()) {
			if(child instanceof Node){
				getChildMap(res, parent, (Node)child, name, raypick);
			}
			if (child.getName()!=null && child.getName().startsWith(name)){
				if(raypick && child instanceof Geometry){
					child.setUserData(ENTITY_GEOMETRY_REFERENCE, parent);
				}
				res.put(child.getName(), child);
			}
		}
	}


	@Override
	protected void initBuilder(Class<T> c) throws Exception {
		model=c.getAnnotation(ModelEntity.class);
	}
}
