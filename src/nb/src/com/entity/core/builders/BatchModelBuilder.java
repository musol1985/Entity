package com.entity.core.builders;

import com.entity.anot.entities.BatchModelEntity;
import com.entity.core.IBuilder;
import com.entity.core.items.BatchModel;

public class BatchModelBuilder extends BaseModelBuilder<BatchModel>{
	private BatchModelEntity anot;

	@Override
	protected void initBuilder(Class<BatchModel> c) throws Exception {
		anot=c.getAnnotation(BatchModelEntity.class);      
	}        
	
	
	@Override
	public void loadInjectors(Class<BatchModel> c) throws Exception {
		super.loadInjectors(c);          
	}


	@Override
    public void onInstance(BatchModel e, IBuilder builder, Object[] params) throws Exception {
        e.setName(anot.name());
        e.setAutoBatch(anot.autoBatch());
        super.onInstance(e, builder, params);        		
    }

}
