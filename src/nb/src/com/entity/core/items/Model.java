package com.entity.core.items;

import com.entity.anot.BuilderDefinition;
import com.entity.core.builders.ModelBuilder;

@BuilderDefinition(builderClass=ModelBuilder.class)
public abstract class Model<T extends ModelBase> extends ModelBase<T, ModelBuilder>{


}	
