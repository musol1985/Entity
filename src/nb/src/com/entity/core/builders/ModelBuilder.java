package com.entity.core.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.entity.anot.Instance;
import com.entity.anot.OnCollision;
import com.entity.anot.RayPick;
import com.entity.anot.RunGLThread;
import com.entity.anot.components.model.RigidBodyComponent;
import com.entity.anot.components.model.SubModelComponent;
import com.entity.anot.components.model.collision.CompBoxCollisionShape;
import com.entity.anot.components.model.collision.CompSphereCollisionShape;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.Injector;
import com.entity.core.injectors.CameraInjector;
import com.entity.core.injectors.EntityInjector;
import com.entity.core.injectors.InputInjector;
import com.entity.core.injectors.LightInjector;
import com.entity.core.injectors.MaterialInjector;
import com.entity.core.injectors.TerrainInjector;
import com.entity.core.injectors.TriggerInjector;
import com.entity.core.injectors.UpdateInjector;
import com.entity.core.items.Model;
import com.entity.test.BoxCollisionShape;
import com.entity.test.CollisionShape;
import com.entity.test.PhysicsRigidBody;
import com.entity.test.SphereCollisionShape;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ModelBuilder extends Builder<Model>{
	public static final String ENTITY_MODEL_REFERENCE="EntityModelReference";
	public static final String ENTITY_GEOMETRY_REFERENCE="EntityGeometryReference";
	
	private boolean mustEnhance;
	
	private ModelEntity model;

	private List<Field> subModels=new ArrayList<Field>();
	
	private List<Field> rigidBody=new ArrayList<Field>();
	
	private HashMap<Class<Model>, Method> collisions=new HashMap<Class<Model>, Method>();

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
		System.out.println(f.getName());
		
		if(f.isAnnotationPresent(RigidBodyComponent.class)){
			f.setAccessible(true);
			rigidBody.add(f);
		}else if(f.isAnnotationPresent(SubModelComponent.class)){
			f.setAccessible(true);
			subModels.add(f);
		}
		
	}

	@Override
	public void loadMethod(Class<Model> c, Method m) throws Exception {
		if(!mustEnhance){
			if(m.isAnnotationPresent(RunGLThread.class)){
				mustEnhance=true;
                        }else if(m.isAnnotationPresent(Instance.class)){
                            mustEnhance=true;
                        }else if(m.isAnnotationPresent(RayPick.class)){
                            mustEnhance=true;
                        }
		}
		if(m.isAnnotationPresent(OnCollision.class)){
			collisions.put((Class<Model>) m.getParameterTypes()[0], m);
		}	
	}

    @Override
    public void onInstance(Model e, IBuilder builder) throws Exception {
        super.onInstance(e, builder);
        		injectModel(e);
		injectRigidBodies(e);
		//injectTerrain(e);
		//injectMaterials(e);
		//injectLights(e);
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
	
	private void injectRigidBodies(Model e)throws Exception{
		for(Field f:rigidBody){
			RigidBodyComponent anot=f.getAnnotation(RigidBodyComponent.class);	
			
			PhysicsRigidBody body=new PhysicsRigidBody(anot.mass(), getCollisionShape(f));
			f.set(e, body);
		}
	}
	
	private CollisionShape getCollisionShape(Field f){
		CollisionShape shape=null;
		
		if(f.isAnnotationPresent(CompSphereCollisionShape.class)){
			shape=new SphereCollisionShape(f.getAnnotation(CompSphereCollisionShape.class).radius());
		}else if(f.isAnnotationPresent(CompBoxCollisionShape.class)){
			CompBoxCollisionShape anot=f.getAnnotation(CompBoxCollisionShape.class);
			
			shape=new BoxCollisionShape(anot.x(), anot.y(), anot.z());
		}
		
		return shape;
	}


	
	public Method collidesWith(Model e){
		return collisions.get(e.getClass());
	}



}
