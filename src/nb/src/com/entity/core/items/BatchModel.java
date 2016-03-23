package com.entity.core.items;

import com.entity.adapters.AutoBatchNode;
import com.entity.anot.BuilderDefinition;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.builders.BatchModelBuilder;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

@BuilderDefinition(builderClass=BatchModelBuilder.class)
public abstract class BatchModel<T extends ModelBase> extends ModelBase<T, BatchModelBuilder>{
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
        
    public void batch(){
        batch.batch();
    }


    @Override
    public void onInstance(IBuilder builder, Object[] params){
    	super.attachChild(batch);
    }
    
	/**
	 * Called when a parent of the tree(this parent's, or his parent, etc) has been dettached
	 * @throws Exception
	 */
	public void onAParentDettached(IEntity parent) throws Exception {
		builder.onDettachInstance(this);	
		//Notify my ModelBase children that a parent has been dettached
		for(Spatial s:batch.getChildren()){
        	if(s instanceof ModelBase){
        		((ModelBase) s).onAParentDettached(this);
        	}
        }
	}
	
	
	/**
	 * Called when a parent of the tree has been attached
	 * @throws Exception
	 */
	public void onAParentAttached(IEntity parent) throws Exception {
		builder.onAttachInstance(this);
		//Notify my ModelBase children that a parent has been attached
		for(Spatial s:batch.getChildren()){
        	if(s instanceof ModelBase){
        		((ModelBase) s).onAParentAttached(this);
        	}
        }		
	}



    @Override
	public int attachChild(Spatial child) {
		return attachEntity((Model)child);
	}


	@Override
	public int detachChild(Spatial child) {
		return dettachEntity((Model)child);
	}
	
	public int attachEntity(Model model){
		int res=batch.attachEntity(model);
		
		try {
			model.onAParentAttached(this);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error on attach entity to a batchModel");
		}
		return res;                        
	}

	public int dettachEntity(Model model){
		int res=batch.detachEntity(model);
		try {
			model.onAParentDettached(this);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error on dettach entity to a batchModel");
		}
		return res;    
	}
	


	public boolean isAutoBatch() {
		return batch.isAutoBatch();
	}


	public void setAutoBatch(boolean autoBatch) {
		batch.setAutoBatch(autoBatch);
	}

}	
