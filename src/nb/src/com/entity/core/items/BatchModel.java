package com.entity.core.items;

import com.entity.adapters.AutoBatchNode;
import com.entity.anot.BuilderDefinition;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.builders.BatchModelBuilder;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

@BuilderDefinition(builderClass=BatchModelBuilder.class)
public abstract class BatchModel extends ModelBase{
	protected AutoBatchNode batch=new AutoBatchNode("",true);
	
	protected boolean isCollidableWith(BatchModel e){
		return false;
	}


	@Override
	public void onAttachToParent(IEntity parent) throws Exception {
		
	}

	@Override
	public Node getNode() {
		return batch;
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
    	super.attachChild(batch);
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
	public int attachChild(Spatial child) {
		return batch.attachEntity((ModelBase)child);
	}


	@Override
	public int detachChild(Spatial child) {
		return batch.detachEntity((ModelBase)child);
	}
	
	public int attachEntity(ModelBase model){
		return batch.attachEntity(model);
	}

	public int dettachEntity(ModelBase model){
		return batch.detachEntity(model);
	}
	@Override
    public void onDettach(IEntity parent)throws Exception{
        
    }


	public boolean isAutoBatch() {
		return batch.isAutoBatch();
	}


	public void setAutoBatch(boolean autoBatch) {
		batch.setAutoBatch(autoBatch);
	}
	
	
}	
