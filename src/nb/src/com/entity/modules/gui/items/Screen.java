package com.entity.modules.gui.items;

import java.util.List;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.components.input.ComposedMouseButtonInput;
import com.entity.anot.components.input.Input;
import com.entity.anot.components.input.MouseButtonInputMapping;
import com.entity.core.EntityManager;
import com.entity.core.interceptors.RayPickInterceptor;
import com.entity.core.items.ModelBase;
import com.entity.modules.gui.builders.ScreenBuilder;
import com.entity.modules.gui.events.ClickEvent;
import com.entity.modules.gui.events.IOnLeftClick;
import com.entity.modules.gui.events.IOnMiddleClick;
import com.entity.modules.gui.events.IOnRightClick;
import com.entity.modules.gui.items.SpriteBase.BUTTON;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

@BuilderDefinition(builderClass=ScreenBuilder.class)
@ComposedMouseButtonInput(inputs = { 
		@MouseButtonInputMapping(action="leftClick", buttons={MouseInput.BUTTON_LEFT}),
		@MouseButtonInputMapping(action="rightClick", buttons={MouseInput.BUTTON_RIGHT}),
		@MouseButtonInputMapping(action="middleClick", buttons={MouseInput.BUTTON_MIDDLE})
})
public class Screen extends ModelBase{
	public void attachSprite(SpriteBase s)throws Exception{
		s.attachToParent(this);
	}
	
	@Input(action="leftClick")
	public void onLeftClick(boolean value, float tpf)throws Exception{
		onClick(BUTTON.LEFT, value, tpf);
	}
	
	@Input(action="rightClick")
	public void onRightClick(boolean value, float tpf)throws Exception{
		onClick(BUTTON.RIGHT, value, tpf);
	}
	
	@Input(action="middleClick")
	public void onMiddleClick(boolean value, float tpf)throws Exception{
		onClick(BUTTON.MIDDLE, value, tpf);
	}
	
	public void onClick(BUTTON button, boolean value, float tpf)throws Exception{
		Vector2f pos=EntityManager.getInputManager().getCursorPosition();
		ClickEvent event=new ClickEvent(button, value, tpf, pos);
		
		boolean captured=clickGUI(getChildren(), event);	
		
		if(!captured){
			CollisionResults results=RayPickInterceptor.rayClickCollisionResult("");
		      
	        for(CollisionResult colision:results){
	        	log.info("RayClick with "+colision.getGeometry().getName());                            
	        	click3D(colision.getGeometry(), event );
			}
		}
	}
	
	public boolean clickGUI(List<Spatial> children, ClickEvent event){
		for(Spatial s:children){
			if(s instanceof SpriteBase){
				if(clickGUI(((SpriteBase)s).getChildren(), event))
					return true;
				
				if(((SpriteBase) s).colisiona(event)){
					return event.click((ModelBase) s);
				}
			}
		}
		return false;
	}
	
	
	
	private static boolean click3D(Spatial s, ClickEvent event){
		ModelBase m=null;
		
		if(s instanceof Geometry){
			m=RayPickInterceptor.getEntityByGeometry((Geometry)s);
		}else if(s instanceof Node && s.getParent()!=null){
			return click3D(s.getParent(), event);
		}else if(s instanceof ModelBase){
			m=(ModelBase)s;
		}
		
		
		if(m!=null){
			if(event.isClickable(m)){
				if(event.click(m))
					return true;
			}
			if(m.getParent()!=null){
				return click3D(m.getParent(),event);
			}
		}else{
			if(s.getParent()!=null){
				return click3D(s.getParent(),event);
			}
		}
		
		return false;
	}

	
}