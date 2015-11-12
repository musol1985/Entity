package com.entity.core.items;

import com.entity.anot.BuilderDefinition;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.builders.ModelBuilder;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

@BuilderDefinition(builderClass=ModelBuilder.class)
public abstract class Model extends Node implements IEntity{
	private IBuilder builder;
	
	protected boolean isCollidableWith(Model e){
		return false;
	}



	@Override
	public void onAttachToParent(IEntity parent) throws Exception {
		
	}



	@Override
	public Node getNode() {
		return this;
	}



	@Override
	public void attachToParent(IEntity parent) throws Exception {
		if(parent.getNode()==null)
			throw new Exception(getClass().getName()+" Cannot attach to a "+parent.getClass().getName());
		
		builder.onAttachInstance(this);
		
		parent.getNode().attachChild(this);
		onAttachToParent(parent);
	}

    @Override
    public void onInstance(IBuilder builder){
    }
    
    @Override
    public void setBuilder(IBuilder builder) {
        this.builder=builder;
    }


	@Override
	public void dettach() throws Exception {
		if(getParent()!=null){
			IEntity parent=(IEntity) getParent();
			
			builder.onDettachInstance(this);
			
			getParent().detachChild(this);
			onDettach(parent);
		}
	}

	@Override
	public void attachChilFromInjector(Spatial s) {
		attachChild(s);
	}    

    @Override
    public void onDettach(IEntity parent)throws Exception{
        
    }
	
	
}	
