package com.entity.core.items;

import java.util.logging.Logger;

import com.entity.adapters.NetSyncAdapter;
import com.entity.anot.BuilderDefinition;
import com.entity.bean.FieldSceneBean;
import com.entity.core.EntityCollisionManager;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.builders.SceneBuilder;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

@BuilderDefinition(builderClass=SceneBuilder.class)
public class Scene<T extends EntityGame> extends AbstractAppState implements IEntity{
	protected static final Logger log = Logger.getLogger(Scene.class.getName());
	
	protected T app;
	
	private PhysicsSpace physics;
	
	
	private EntityCollisionManager collisionListener=new EntityCollisionManager();
	
	private Node node;
	
	private IBuilder builder;
	
	private NetSyncAdapter netSync;
	
	private FieldSceneBean proxy;
	
	public Scene(){
		
	}
	
	public void setParams(Object...params){
		
	}
	
	public void setProxy(FieldSceneBean proxy){
		this.proxy=proxy;
	}
	
	public FieldSceneBean getProxy(){
		return proxy;
	}
	
	public boolean isPreLoaded(){
		return proxy==null;
	}

	
	@Override
	public Node getNode(){
		return node;
	}

	public void setNode(){
		node=new Node(getClass().getName()+"_sceneNode");
	}

	@Override
	public void onInstance(IBuilder builder, Object[] params) {

	}
	
	public void onLoadScene() throws Exception{
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public void initialize(AppStateManager stateManager, Application app) {	
                this.app=(T)app;
		super.initialize(stateManager, app);
				
		EntityManager.setCurrentScene(this);
		getApp().getRootNode().attachChild(node);
		
		builder.onAttachInstance(this);

		initPhysics();
		
		netSync=new NetSyncAdapter();
		
		try{
			onLoadScene();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	private void initPhysics(){		
		BulletAppState appState=app.getStateManager().getState(BulletAppState.class);
		if(appState!=null){
			physics=appState.getPhysicsSpace();
			
			physics.addCollisionListener(collisionListener);
		}
	}


	@Override
	public void cleanup() {
		try{
			if(physics!=null)
				physics.removeCollisionListener(collisionListener);
			
			if(node!=null){
				getApp().getRootNode().detachChild(node);
			}
			
                        if(builder!=null)
                            builder.onDettachInstance(this);
			
			EntityManager.setCurrentScene(null);				
		}catch(Exception e){
			e.printStackTrace();
		}
		
		super.cleanup();
	}
	
	public NetSyncAdapter getNetSync(){
		return netSync;
	}
	
	public T getApp(){
		return app;
	}
	
	public PhysicsSpace getPhysics(){
		return physics;
	}

	
	@Override
	public void onAttachToParent(IEntity parent) throws Exception {
		
	}



	@Override
	public void attachToParent(IEntity parent) throws Exception {
		log.info("Attaching "+this+" scene");
		EntityManager.getGame().getStateManager().attach(this);
		//El onattach se llama en el initialize
	}


	@Override
	public void dettach() throws Exception {
		AbstractAppState scene=EntityManager.getGame().getStateManager().getState(getClass());
		if(scene!=null){
			log.info("Dettaching scene"+this);
			EntityManager.getGame().getStateManager().detach(scene);
		}else{
			log.info("The scene "+this+" is not attached.");
		}
		//El onDettach se llama en el cleanup
	}

	@Override
	public void onDettach(IEntity parent) throws Exception {
		
	}

	
	@Override
	public void attachChilFromInjector(Spatial s) {
		getNode().attachChild(s);
	}    

    @Override
    public void setBuilder(IBuilder builder) {
        this.builder=builder;
    }

	@Override
	public void onPreInject(IBuilder builder, Object[] params) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void setApp(T app){
		this.app=app;
	}
    
}
