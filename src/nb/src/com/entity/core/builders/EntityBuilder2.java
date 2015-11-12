package com.entity.core.builders;



public class EntityBuilder2 {
	/*public static final String ENTITY_MODEL_REFERENCE="EntityModelReference";
	public static final String ENTITY_GEOMETRY_REFERENCE="EntityGeometryReference";
	
	private boolean mustEnhance;
	private List<Field> rigidBody=new ArrayList<Field>();
	
	private Field model;
	private List<Field> subModels=new ArrayList<Field>();
	
	private List<Method> inputsDigital=new ArrayList<Method>();
	private List<Method> inputsAnalog=new ArrayList<Method>();
	
	private HashMap<Class<Entity>, Method> collisions=new HashMap<Class<Entity>, Method>();
	
	private Method update;
	
	private List<TerrainBean> terrains=new ArrayList<TerrainBean>();
		
	private List<MaterialBean> materials=new ArrayList<MaterialBean>();
	
	private List<Field> lights=new ArrayList<Field>();

	@SuppressWarnings("unchecked")
	public EntityBuilder(Class cls)throws Exception{
		for(Method m:cls.getMethods()){
			if(!mustEnhance){
				if(m.isAnnotationPresent(RunGLThread.class))
					mustEnhance=true;
			}
			if(m.isAnnotationPresent(Input.class)){
				if(m.getParameterTypes()[2]==Boolean.class){
					inputsDigital.add(m);
				}else{
					inputsAnalog.add(m);
				}
			}else if(m.isAnnotationPresent(OnUpdate.class)){
				if(update!=null)
					throw new Exception("Only 1 onUpdate annotation allowed");
				update=m;
			}else if(m.isAnnotationPresent(OnCollision.class)){
				collisions.put((Class<Entity>) m.getParameterTypes()[0], m);
			}
			
		}
		
		for(Field f:cls.getDeclaredFields()){
			System.out.println(f.getName());
			if(f.isAnnotationPresent(RigidBodyComponent.class)){
				f.setAccessible(true);
				rigidBody.add(f);
			}else if(f.isAnnotationPresent(ModelComponent.class)){
				if(model!=null)
					throw new Exception("Only 1 model component for entity allowed");
				f.setAccessible(true);
				model=f;
			}else if(f.isAnnotationPresent(SubModelComponent.class)){
				f.setAccessible(true);
				subModels.add(f);
			}else if(f.isAnnotationPresent(TerrainComponent.class)){
				f.setAccessible(true);
				terrains.add(new TerrainBean(f, cls, f.getAnnotation(TerrainComponent.class)));
			}
			if(f.isAnnotationPresent(MaterialComponent.class)){
				f.setAccessible(true);
				materials.add(new MaterialBean(f, cls, f.getAnnotation(MaterialComponent.class)));
			}
			if(EntityManager.isLight(f)){
				f.setAccessible(true);
				lights.add(f);
			}
		}
						
	}

	
	public void onInstance(Entity e){
		System.out.println("on instance entity template");
		try{			
			injectModel(e);
			injectRigidBodies(e);
			mapInputs(e);
			injectUpdate(e);
			injectTerrain(e);
			injectMaterials(e);
			injectLights(e);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void injectMaterials(final Entity e) throws Exception{
		for(MaterialBean m:materials){
			m.onLoad(e);
		}
	}
	
	private void injectLights(final Entity e) throws Exception{
		for(Field f:lights){
			Light l=EntityManager.loadLight(f, e);
			
		}
	}
	
	private void injectUpdate(final Entity e)throws Exception{
		if(update!=null && model!=null){
			e.addControl(new ControlAdapter() {
				@Override
				public void update(float tpf) {
					try{
						update.invoke(e, tpf);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	private void injectTerrain(final Entity e)throws Exception{
		for(TerrainBean t:terrains){
			TerrainQuad terrain = new TerrainQuad(t.getName(), t.getAnot().chunkSize()+1, t.getAnot().realSize()+1, t.getHeight(e, EntityManager.getAssetManager()).getHeightMap());
			
			if(t.getAnot().LOD()){
				terrain.addControl(new TerrainLodControl(terrain, EntityManager.getCurrentScene().getApp().getCamera()));
			}
			
			t.getTerrainField().set(e, terrain);
			
			if(t.getAnot().attach())
				e.attachChild(terrain);
		}
	}
	
	private void injectModel(Entity e)throws Exception{
		if(model!=null){									
			ModelComponent anot=model.getAnnotation(ModelComponent.class);
			
			Node n=(Node) EntityManager.getAssetManager().loadModel(anot.asset());
			
			if(anot.name().length()>0)
				n.setName(anot.name());
			
			if(anot.smartReference())
				n.setUserData(ENTITY_MODEL_REFERENCE, e);
			
			model.set(e, n);
			
			for(Field f:subModels){
				SubModelComponent a=f.getAnnotation(SubModelComponent.class);
				Spatial s=n.getChild(a.name());
				if(a.rayPickResponse() && s instanceof Geometry){
					s.setUserData(ENTITY_GEOMETRY_REFERENCE, e);
				}
				
				f.set(e, s);
			}
			
			if(anot.attach())
				e.attachChild(n);
		}
	}
	
	private void injectRigidBodies(Entity e)throws Exception{
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
	
	
	private void mapInputs(final Entity e)throws Exception{
		for(final Method m:inputsAnalog){			
			InputListener listener=new AnalogListener() {
				public void onAnalog(String arg0, float arg1, float tpf) {
					try {
						m.invoke(e, new Object[]{arg1, tpf});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
			};
			EntityManager.getCurrentScene().getApp().getInputManager().addListener(listener, m.getAnnotation(Input.class).action());	
		}
		
		for(final Method m:inputsDigital){			
			InputListener listener=new ActionListener() {
				public void onAction(String arg0, boolean arg1, float arg2) {
					try {
						m.invoke(e, new Object[]{arg1, arg2});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			EntityManager.getCurrentScene().getApp().getInputManager().addListener(listener, m.getAnnotation(Input.class).action());	
		}
	}
	

	public boolean isMustEnhance(){
		return mustEnhance;
	}
	
	public Method collidesWith(Entity e){
		return collisions.get(e.getClass());
	}
	
	public void attachEntity(Entity e, Scene scene)throws Exception{
		for(Field f:rigidBody){
			scene.getPhysics().add(f.get(e));
		}
		for(Field f:lights){
			e.addLight((Light) f.get(e));
		}
	}*/
}
