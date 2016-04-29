package com.entity.modules.gui.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.core.builders.BaseModelBuilder;
import com.entity.modules.gui.injectors.SpriteInjector;

public class ScreenBuilder extends BaseModelBuilder{

    @Override
    public void loadInjectors(Class c) throws Exception {
        addInjector(new SpriteInjector());
    }

	@Override
	public boolean isMustEnhance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loadField(Class c, Field f) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadMethod(Class c, Method m) throws Exception {
		// TODO Auto-generated method stub
		
	}

}