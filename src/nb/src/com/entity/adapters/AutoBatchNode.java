package com.entity.adapters;

import com.entity.core.IEntity;
import com.entity.core.items.ModelBase;
import com.jme3.scene.BatchNode;

public class AutoBatchNode extends BatchNode{
	protected boolean autoBatch;
	
	public AutoBatchNode(String name, boolean autoBatch){
		super(name);
		this.autoBatch=autoBatch;
	}

	public boolean isAutoBatch() {
		return autoBatch;
	}

	public void setAutoBatch(boolean autoBatch) {
		this.autoBatch = autoBatch;
	}


	public int attachEntity(ModelBase child) {
		int res=attachChild(child);
		if(autoBatch)
			batch();
		return res;
	}

	public int detachEntity(ModelBase child) {
		int res=detachChild(child);
		if(autoBatch)
			batch();
		return res;
	}
	
	
}
