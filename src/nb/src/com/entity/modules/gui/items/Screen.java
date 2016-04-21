package com.entity.modules.gui.items;

import com.entity.anot.BuilderDefinition;
import com.entity.core.items.ModelBase;
import com.entity.modules.gui.builders.ScreenBuilder;

@BuilderDefinition(builderClass=ScreenBuilder.class)
public class Screen extends ModelBase{
	public void attachSprite(SpriteBase s)throws Exception{
		s.attachToParent(this);
	}
}