package com.entity.core.items;

import com.entity.adapters.AutoBatchNode;
import com.entity.anot.BuilderDefinition;
import com.entity.core.IEntity;
import com.entity.core.builders.ModelBuilder;
import com.jme3.scene.Spatial;

@BuilderDefinition(builderClass=ModelBuilder.class)
public abstract class Model extends ModelBase<ModelBuilder>{




	public void attachToBatchNode(AutoBatchNode n){
		builder.onAttachInstance(this);
		
		n.attachEntity(this);
	}

   


}	
