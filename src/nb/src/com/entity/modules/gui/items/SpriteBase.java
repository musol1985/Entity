/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.modules.gui.items;



import com.entity.anot.BuilderDefinition;
import com.entity.core.EntityManager;
import com.entity.core.builders.BaseModelBuilder;
import com.entity.core.items.ModelBase;
import com.entity.modules.gui.GUIGame;
import com.entity.modules.gui.builders.SpriteBuilder;
import com.entity.modules.gui.events.ClickEvent;
import com.entity.modules.gui.events.ClickInterceptor;
import com.entity.modules.gui.events.MoveEvent;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;

/**
 *
 * @author Edu
 */
@BuilderDefinition(builderClass=SpriteBuilder.class)
public abstract class SpriteBase<G extends Spatial> extends ModelBase<SpriteBase, BaseModelBuilder>{
	
	public enum BUTTON{LEFT, RIGHT, MIDDLE};
	
    protected G geo;
    protected float width  = 1f;
    protected float height = 1f;
    protected ColorRGBA color;
    protected float size=1f;
    protected ClickInterceptor interceptor;    

    
    protected void instance(String name,  G geo, String texture) {
        setName(name);
        this.geo=geo;
        create();
        
        setImage(texture, true);
    }
    

    private void create(){
        setQueueBucket(RenderQueue.Bucket.Gui);
        setCullHint(CullHint.Never);
        color=new ColorRGBA(1,1,1,1);
        
        attachChild(geo);
    }
    
    public GUIGame getGUI(){
    	return EntityManager.getGame().getGUI();
    }
    
    public int getX(){
        return (int)(getLocalTranslation().x/getGUI().ratioW);
    }
    
    public int getY(){
        return (int)(getLocalTranslation().y/getGUI().ratioH);
    }
    
    public void setPosition(float x, float y) {
        setPosition((int)x, (int)y);
    }
    
    public void setPosition(int x, int y){
        setLocalTranslation(x*getGUI().ratioW, y*getGUI().ratioH, getLocalTranslation().getZ());
    }
    
    public float getWidth() {
        return (width/getGUI().ratioW);
    }

    public float getHeight() {
        return (height/getGUI().ratioH);
    }
    
    public void setWidth(float width){
        width=width*getGUI().ratioW;
        this.width = width;
        geo.setLocalScale(new Vector3f(width, height, 1f));
    }
    
    public void setHeight(float height){
        height=height*getGUI().ratioH;
        this.height = height;
        geo.setLocalScale(new Vector3f(width, height, 1f));
    }
    
    public void setImage(String imgName, boolean useAlpha){
        setName(imgName);
        TextureKey key = new TextureKey(imgName, true);
        Texture2D tex = (Texture2D) EntityManager.getAssetManager().loadTexture(key);
        
        //tex.setWrap(Texture.WrapMode.MirroredRepeat);
        
        setWidth(tex.getImage().getWidth());
        setHeight(tex.getImage().getHeight());
        
        setTexture(tex, useAlpha);
    }
    
    public void setTexture(Texture2D tex, boolean useAlpha){
        Geometry geo=(Geometry)this.geo;
        if (geo.getMaterial() == null){
            Material mat = new Material(EntityManager.getAssetManager(), "Common/MatDefs/Gui/Gui.j3md");
            mat.setColor("Color", new ColorRGBA(ColorRGBA.White));
            geo.setMaterial(mat);
        }
        geo.getMaterial().getAdditionalRenderState().setBlendMode(useAlpha ? RenderState.BlendMode.Alpha : RenderState.BlendMode.Off);
        geo.getMaterial().setTexture("Texture", tex);
    } 
    
    public void setColor(ColorRGBA color){
        this.color=color;
        if(geo instanceof Geometry){
            ((Geometry)geo).getMaterial().setColor("Color", color);
        }
    } 
    
    public void setSize(float size) {
        this.size=size;
        setLocalScale(getWidth()*size*getGUI().ratioW,getHeight()*size*getGUI().ratioH, getLocalScale().z);
    }
    
    public void rotate(float r) {
        super.rotate(0, 0, r);
    }
    
    public float getSize(){
        return size;
    }
    
    public void setAlpha(float alpha){
        color.a=alpha;        
        setColor(color);
    }
    
    public float getAlpha(){
        return color.a;
    }

    public void centerX() {
        setPosition(getGUI().width/2, getY());
    }

    public void centerY() {
         setPosition(getX() ,getGUI().height/2);
    }
    
    public void centerXY(){
         setPosition(getGUI().width/2 ,getGUI().height/2);
    }
    
    public void move(int x, int y){
        setPosition(getX()+x, getY()+y);
    }
    
    public void centerInParent(){
        if(isParentASprite()){
            setPosition(getParentModel().getWidth()/2-getWidth()/2, getParentModel().height/2-getHeight()/2);
        }
    }
    
    private boolean isParentASprite(){
    	return getParent() instanceof SpriteBase;
    }
    
    public boolean isIn(Vector2f pos){
    	return pos.x>=getX()+getWidth()/4 && pos.x<getX()+getWidth()+getWidth()/2 && pos.y>=getY()+getHeight()/4 && pos.y<getY()+getHeight()+getHeight()/2;
    }
    
    public boolean colisiona(ClickEvent event){ 
        return isIn(event.pos);      
    }
    
    public boolean onClick(ClickEvent event)throws Exception{
    	if(interceptor!=null)
    		return interceptor.onClick(event, this);
    	return false;
    }
    
    public boolean isClickInterceptor(){
    	return interceptor!=null;
    }


	public void setInterceptor(ClickInterceptor interceptor) {
		this.interceptor = interceptor;
	}
    
    public boolean isMouseIn(MoveEvent event){
    	return isIn(event.getPos()) && !isIn(event.getOldPos());
    }
    
    public boolean isMouseOut(MoveEvent event){
    	return !isIn(event.getPos()) && isIn(event.getOldPos());
    }
    
    public boolean isMouseMove(MoveEvent event){
    	return isIn(event.getPos()) && isIn(event.getOldPos());
    }
}
