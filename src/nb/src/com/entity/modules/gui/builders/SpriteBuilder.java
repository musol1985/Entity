package com.entity.modules.gui.builders;

import com.entity.core.builders.BaseModelBuilder;
import com.entity.modules.gui.injectors.ButtonInjector;
import com.entity.modules.gui.injectors.SpriteInjector;

public class SpriteBuilder extends BaseModelBuilder{

    @Override
    public void loadInjectors(Class c) throws Exception {
        addInjector(new SpriteInjector());
        addInjector(new ButtonInjector());
    }

	@Override
	protected void initBuilder(Class c) throws Exception {
		// TODO Auto-generated method stub
		
	}


}