package com.entity.modules.gui.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.core.builders.BaseModelBuilder;
import com.entity.modules.gui.anot.ScreenEntity;
import com.entity.modules.gui.anot.ScreenGUI;
import com.entity.modules.gui.injectors.SpriteInjector;
import com.entity.modules.gui.items.Screen;

public class ScreenBuilder extends BaseModelBuilder<Screen>{
	private ScreenEntity anot;
	

	@Override
	protected void initBuilder(Class<Screen> c) throws Exception {
		anot=(ScreenEntity) c.getAnnotation(ScreenEntity.class);                
	}

	
    @Override
    public void loadInjectors(Class<Screen> c) throws Exception {
        super.loadInjectors(c);
        addInjector(new SpriteInjector<Screen>());
    }

	@Override
	public boolean isMustEnhance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loadField(Class<Screen> c, Field f) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadMethod(Class<Screen> c, Method m) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public ScreenEntity getAnot() {
		return anot;
	}

	
}