package com.entity.modules.gui.events;

import com.entity.core.items.ModelBase;
import com.entity.modules.gui.items.SpriteBase.BUTTON;

public interface IDraggable {
	public void onDrag(ModelBase over)throws Exception;
	public void onDrop(ModelBase over, BUTTON button)throws Exception;
	public void onDragging(ModelBase over)throws Exception;
}
