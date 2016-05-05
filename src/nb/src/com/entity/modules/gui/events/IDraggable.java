package com.entity.modules.gui.events;

import com.entity.core.items.ModelBase;
import com.entity.modules.gui.items.SpriteBase.BUTTON;

public interface IDraggable {
	public void onDrag()throws Exception;
	/**
	 * Event on drop, returns cancel drop
	 * @param button
	 * @return  true  -> Cancel drop 
	 * 			false -> Confirm drop
	 * @throws Exception
	 */
	public boolean onDrop(BUTTON button)throws Exception;
	public void onDragging(ModelBase over)throws Exception;
}
