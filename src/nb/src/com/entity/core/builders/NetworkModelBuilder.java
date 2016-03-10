package com.entity.core.builders;

import com.entity.core.injectors.field.SyncMessageInjector;
import com.entity.core.items.Model;
import com.entity.core.items.NetworkModel;

public class NetworkModelBuilder extends ModelBuilder<Model>{

	@Override
	public void loadInjectors(Class<Model> c) throws Exception {
		addInjector(new SyncMessageInjector<NetworkModel>());
		super.loadInjectors(c);		
	}
		
}
