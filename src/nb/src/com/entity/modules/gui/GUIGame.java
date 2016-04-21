package com.entity.modules.gui;

import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.modules.gui.anot.GUI;
import com.entity.modules.gui.items.Screen;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class GUIGame extends Node implements IEntity{

	public int width;
    public int height;
    public float centerW;
    public float centerH;
    
    public float ratio;
    public float ratioW;
    public float ratioH;
    
    private Screen screen;
	
	public GUIGame(GUI gui){
		super("GUI_GAME_NODE");

		width=gui.canvas()[0];
		height=gui.canvas()[1];
		
		centerW=width/2;
		centerH=height/2;
		
		int RATIO=width/height;
		ratio=((float)EntityManager.getGame().getContext().getSettings().getWidth()/(float)EntityManager.getGame().getContext().getSettings().getHeight())-RATIO+1f;
        ratioW=EntityManager.getGame().getContext().getSettings().getWidth()/ratioW;
        ratioH=EntityManager.getGame().getContext().getSettings().getHeight()/ratioH;  
        
        EntityManager.getGame().getGuiNode().attachChild(this);		
	}
	
	public Screen getScreen(){
		return screen;
	}
	
	public void setScreen(Screen s)throws Exception{
		if(screen!=null){
			screen.dettach();
		}		
		screen=s;
		screen.attachToParent(this);
	}

	@Override
	public void onAttachToParent(IEntity parent) throws Exception {
		
	}

	@Override
	public Node getNode() {
		return this;
	}

	@Override
	public void attachToParent(IEntity parent) throws Exception {
		
	}

	@Override
	public void dettach() throws Exception {
		EntityManager.getGame().getGuiNode().detachChild(this);
	}

	@Override
	public void onDettach(IEntity parent) throws Exception {
		
	}

	@Override
	public void onInstance(IBuilder builder, Object[] params) {
		
	}

	@Override
	public void onInstanceCache(IBuilder builder, Object[] params) throws Exception {
		
	}

	@Override
	public void onPreInject(IBuilder builder, Object[] params) throws Exception {
		
	}

	@Override
	public void setBuilder(IBuilder builder) {
		
	}

	@Override
	public void attachChilFromInjector(Spatial s) {
		
	}
	
}
