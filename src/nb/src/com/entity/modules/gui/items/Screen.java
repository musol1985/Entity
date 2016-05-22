package com.entity.modules.gui.items;

import java.util.List;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Conditional;
import com.entity.anot.components.input.ComposedMouseButtonInput;
import com.entity.anot.components.input.ComposedMouseMoveInputMapping;
import com.entity.anot.components.input.Input;
import com.entity.anot.components.input.MouseButtonInputMapping;
import com.entity.anot.components.input.MouseMoveInputMapping;
import com.entity.bean.OnPickModel;
import com.entity.core.EntityManager;
import com.entity.core.interceptors.RayPickInterceptor;
import com.entity.core.items.ModelBase;
import com.entity.modules.gui.builders.ScreenBuilder;
import com.entity.modules.gui.events.ClickEvent;
import com.entity.modules.gui.events.IDraggable;
import com.entity.modules.gui.events.IOnMouseMove;
import com.entity.modules.gui.events.MoveEvent;
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
@ComposedMouseMoveInputMapping(inputs={
		@MouseMoveInputMapping(action="mouseMoveX", axis={MouseInput.AXIS_X}),
                @MouseMoveInputMapping(action="mouseMoveXNeg", axis={MouseInput.AXIS_X}, negate = true),
		@MouseMoveInputMapping(action="mouseMoveY", axis={MouseInput.AXIS_Y}),
                @MouseMoveInputMapping(action="mouseMoveYNeg", axis={MouseInput.AXIS_Y}, negate = true)
})
public class Screen extends ModelBase<Screen, ScreenBuilder>{
	
	private ModelBase drag;//Element to drag
	private Class<? extends ModelBase> drop;//Element where the drag will drop(terrain)
	private Node in;//Node where to calculate raypicks(rootNode)
	
	private MoveEvent me=new MoveEvent();
	
	public void attachSprite(SpriteBase s)throws Exception{
		s.attachToParent(this);
	}
	
	public boolean captureClick(){
		if(builder.getAnot()!=null)
			return builder.getAnot().captureClick();
		return true;
	}
	
	public boolean captureMove(){
		if(builder.getAnot()!=null)
			return builder.getAnot().captureMove();
		return true;
	}
	
	@Input(action="leftClick")
	@Conditional(method="captureClick")
	public void onLeftClick(boolean value, float tpf)throws Exception{
		onClick(BUTTON.LEFT, value, tpf);
	}
	
	@Input(action="rightClick")
	@Conditional(method="captureClick")
	public void onRightClick(boolean value, float tpf)throws Exception{
		onClick(BUTTON.RIGHT, value, tpf);
	}
	
	@Input(action="middleClick")
	@Conditional( method="captureClick")
	public void onMiddleClick(boolean value, float tpf)throws Exception{
		onClick(BUTTON.MIDDLE, value, tpf);
	}
	
    @Input(action = "mouseMoveX")
    @Conditional(method="captureMove")
    public void mouseMove(float value, float tpf)throws Exception{
    	onMouseMove(tpf);
    }
    
    @Input(action = "mouseMoveY")
    @Conditional(method="captureMove")
    public void mouseMoveY(float value, float tpf)throws Exception{
    	onMouseMove(tpf);
    }
    
    @Input(action = "mouseMoveXNeg")
    @Conditional(method="captureMove")
    public void mouseMoveNeg(float value, float tpf)throws Exception{
    	onMouseMove(tpf);
    }
    
    @Input(action = "mouseMoveYNeg")
    @Conditional(method="captureMove")
    public void mouseMoveYNeg(float value, float tpf)throws Exception{
    	onMouseMove(tpf);
    }
    
    private void onMouseMove(float tpf)throws Exception{
    	me.update(EntityManager.getInputManager().getCursorPosition(), tpf);
    	if(drag!=null){
            
    		OnPickModel plain=RayPickInterceptor.rayPickOver(drop, in);

    		if(plain!=null){
    			drag.setLocalTranslation(plain.getCollision().getContactPoint());
    			if(drag instanceof IDraggable){
    				((IDraggable)drag).onDragging(plain.getM());
    			}
    		}
    	}else{
    		moveGUI(getChildren());
    	}
    }
    
    
    public boolean moveGUI(List<Spatial> children)throws Exception{
		for(Spatial s:children){
                    if(s instanceof SpriteBase){
                        if(moveGUI(((SpriteBase)s).getChildren()))
                                return true;
                        if(s instanceof IOnMouseMove){
                            if(((SpriteBase) s).isMouseIn(me)){
                                    ((IOnMouseMove) s).onIn(me);
                            }else if(((SpriteBase) s).isMouseMove(me)){
                                    ((IOnMouseMove) s).onMove(me);
                            }else if(((SpriteBase) s).isMouseOut(me)){
                                    ((IOnMouseMove) s).onOut(me);
                            }
                        }
                    }
		}
		return false;
	}
	
	public void onClick(BUTTON button, boolean value, float tpf)throws Exception{
            if(value)
		if(drag==null){
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
		}else{
			drop(button);
		}
	}
	
	public boolean clickGUI(List<Spatial> children, ClickEvent event)throws Exception{
		for(Spatial s:children){
			if(s instanceof SpriteBase){
				if(clickGUI(((SpriteBase)s).getChildren(), event))
					return true;
				
				if(((SpriteBase) s).colisiona(event)){
					return event.click((SpriteBase) s);
				}
			}
		}
		return false;
	}
	
	
	
	private static boolean click3D(Spatial s, ClickEvent event)throws Exception{
		ModelBase m=null;
		
		if(s instanceof Geometry){
			m=RayPickInterceptor.getEntityByGeometry((Geometry)s);
		}else if(s instanceof Node && s.getParent()!=null){
			return click3D(s.getParent(), event);
		}else if(s instanceof ModelBase){
			m=(ModelBase)s;
		}
		
		
		if(m!=null){
			if(event.click(m))
				return true;
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

	/**
	 * Drags a model over a plane model(terrain) 
	 * @param model -> Model to drag&drop
	 * @param plain -> Plain where the model will move(terrain)
	 * @param in    -> Node where to calculate raypicks(rootNode)
	 */
	public void drag(ModelBase model, Class<? extends ModelBase> plain, Node in)throws Exception{
		this.drag=model;
		this.drop=plain;
		this.in=in;
		
		if(model instanceof IDraggable){
			((IDraggable)model).onDrag();
		}
	}
	
	public void drop(BUTTON button)throws Exception{
		boolean cancel=false;
		if(drag instanceof IDraggable){
			cancel=((IDraggable)drag).onDrop(button);
		}
		if(!cancel){
			this.drag=null;
			this.drop=null;
			this.in=null;
		}
	}
}