package com.entity.core.items;

import com.entity.adapters.NetSyncAdapter;
import com.entity.anot.BuilderDefinition;
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
public abstract class Scene<T extends EntityGame> extends AbstractAppState implements IEntity{
	protected T app;
	
	private PhysicsSpace physics;
	
	
	private EntityCollisionManager collisionListener=new EntityCollisionManager();
	
	private Node node;
	
	private IBuilder builder;
	
	private NetSyncAdapter netSync;

	
	@Override
	public Node getNode(){
		return node;
	}

	public void setNode(){
		node=new Node(getClass().getName()+"_sceneNode");
	}

	@Override
	public void onInstance(IBuilder builder) {

	}
	
	public abstract void loadScene() throws Exception;


	@SuppressWarnings("unchecked")
	@Override
	public void initialize(AppStateManager stateManager, Application app) {	
		super.initialize(stateManager, app);
		
		this.app=(T)app;
		EntityManager.setCurrentScene(this);
		getApp().getRootNode().attachChild(node);
		
		builder.onAttachInstance(this);

		initPhysics();
		
		netSync=new NetSyncAdapter();
		
		try{
			loadScene();
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
		System.out.println("Attaching "+this+" scene");
		EntityManager.getGame().getStateManager().attach(this);
		//El onattach se llama en el initialize
	}


	@Override
	public void dettach() throws Exception {
		AbstractAppState scene=EntityManager.getGame().getStateManager().getState(getClass());
		if(scene!=null){
			System.out.println("Dettaching scene"+this);
			EntityManager.getGame().getStateManager().detach(scene);
		}else{
			System.out.println("The scene "+this+" is not attached.");
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
	public void onPreInject(IBuilder builder) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
    
}
