/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.adapters;

import java.util.ArrayList;
import java.util.List;

import com.entity.adapters.listeners.ICameraUpdate;
import com.entity.adapters.listeners.IFollowCameraListener;
import com.entity.anot.CamNode;
import com.entity.anot.FollowCameraNode;
import com.entity.anot.OnUpdate;
import com.entity.anot.components.input.ComposedMouseMoveInputMapping;
import com.entity.anot.components.input.Input;
import com.entity.anot.components.input.MouseButtonInputMapping;
import com.entity.anot.components.input.MouseMoveInputMapping;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.items.Model;
import com.entity.core.items.ModelBase;
import com.jme3.input.MouseInput;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Edu
 */
@ModelEntity
@ComposedMouseMoveInputMapping(inputs = {
	    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_MOVE_X,axis = {MouseInput.AXIS_X}),
	    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_MOVE_X,axis = {MouseInput.AXIS_X}, negate = true),
	    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_WHEEL_POSITIVE,axis = {MouseInput.AXIS_WHEEL}, negate = true),
	    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_WHEEL_NEGATIVE,axis = {MouseInput.AXIS_WHEEL})	       
	})
@MouseButtonInputMapping(action=ScrollCameraAdapter.MOUSE_WHEEL_CLICK, buttons={MouseInput.BUTTON_MIDDLE})
public class FollowCameraAdapter extends Model{
	public static final String MOUSE_MOVE_X="mouseMoveX";
	public static final String MOUSE_MOVE_Y="mouseMoveY";
	public static final String MOUSE_WHEEL_POSITIVE="mouseWheel+";
	public static final String MOUSE_WHEEL_NEGATIVE="mouseWheel-";
	public static final String MOUSE_WHEEL_CLICK="mouseWheelClick";
	
    @CamNode(name="FollowCamera")
    private CameraNode camNode;
    
    private int rotateSpeed=20;
    
    private boolean wheelClick;
    
    private Vector3f tmp=new Vector3f();
    private IFollowCameraListener listener;
    private Geometry debug;
    
    private List<ICameraUpdate> updates=new ArrayList<ICameraUpdate>();
    private Vector3f oldPos=new Vector3f();

    @Override
    public void onInstance(IBuilder builder, Object[] params) {
        EntityManager.getGame().getFlyByCamera().setMoveSpeed(100);    
        EntityManager.getGame().getFlyByCamera().setEnabled(false);
        
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camNode.setEnabled(true);
        
        attachChild(camNode);
        camNode.setLocalTranslation(0, 40, -40);
        camNode.setLocalRotation(new Quaternion().fromAngles(FastMath.HALF_PI/2, 0, 0));
    }
    
    @Input(action = MOUSE_MOVE_X)
    public void mouseMove(float valor, float tpf){
    	if(wheelClick){
    		if(valor>0){
    			rotate(0, rotateSpeed*tpf, 0);    	
    			callUpdates();
    		}else if(valor<0){
    			rotate(0, -rotateSpeed*tpf, 0);
    			callUpdates();
    		}
    	}
    }

    @Input(action = MOUSE_WHEEL_POSITIVE)
    public void mouseWheel(float valor, float tpf){
        camNode.move(0,5*valor,-5*valor);
        callUpdates();
    }
    
    @Input(action = MOUSE_WHEEL_NEGATIVE)
    public void mouseWheelNeg(float valor, float tpf){
        camNode.move(0,-5*valor,5*valor);
        callUpdates();
    }
    
    @Input(action = MOUSE_WHEEL_CLICK)
    public void mouseWheelClick(boolean valor, float tpf){
    	wheelClick=valor;
    }

    @Override
    public void onDettach(IEntity parent) throws Exception {
        camNode.setEnabled(false);
    }       
    
	public void setValues(FollowCameraNode anot){
		setRotateSpeed(anot.rotateSpeed());
	}
    
    public int getRotateSpeed() {
		return rotateSpeed;
	}

	public void setRotateSpeed(int rotateSpeed) {
		this.rotateSpeed = rotateSpeed;
	}



	public IFollowCameraListener getListener() {
		return listener;
	}



	public void setListener(IFollowCameraListener listener) {
		this.listener = listener;
	}

	private void createDebug(){
            debug = new Geometry("ScrollCamDEbug", new Sphere(30,30,1));

            Material mat = new Material(EntityManager.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Red);
            debug.setMaterial(mat);      
	}

   public void debug(boolean active){
	   if(active){
		   if(debug==null)
			   createDebug();
		   if(debug.getParent()==null)
			   attachChild(debug);
	   }else{
		   if(debug!=null && debug.getParent()!=null)
			   detachChild(debug);
	   }
    } 
   
   public void followTo(ModelBase model)throws Exception{			
		attachToParent(model);
   }
   
    public void followTo(Node model)throws Exception{			
	model.attachChild(this);
   }
   
   @OnUpdate
   private void update(float tpf){

       if(listener!=null){
       	listener.onUpdate(this);
       }
       if(!oldPos.equals(getWorldTranslation())){
           callUpdates();
       }
   }
   
   public void addUpdate(ICameraUpdate update){
	   updates.add(update);
   }
   
   public void removeUpdate(ICameraUpdate update){
	   updates.remove(update);
   }
   
   public void callUpdates(){
	   if(updates.size()>0){
		   for(ICameraUpdate upd:updates){
			   upd.onPositionUpdate(camNode.getCamera());
		   }
	   }
   }
}

