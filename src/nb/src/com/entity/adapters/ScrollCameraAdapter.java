/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.adapters;

import com.entity.anot.CamNode;
import com.entity.anot.OnUpdate;
import com.entity.anot.ScrollCameraNode;
import com.entity.anot.components.input.ComposedMouseMoveInputMapping;
import com.entity.anot.components.input.Input;
import com.entity.anot.components.input.MouseButtonInputMapping;
import com.entity.anot.components.input.MouseMoveInputMapping;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.items.Model;
import com.jme3.input.MouseInput;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;

/**
 *
 * @author Edu
 */
@ModelEntity
@ComposedMouseMoveInputMapping(inputs = {
    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_MOVE_X,axis = {MouseInput.AXIS_X}),
    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_MOVE_X,axis = {MouseInput.AXIS_X}, negate = true),
    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_WHEEL_POSITIVE,axis = {MouseInput.AXIS_WHEEL}, negate = true),
    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_WHEEL_NEGATIVE,axis = {MouseInput.AXIS_WHEEL}),
    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_MOVE_Y,axis = {MouseInput.AXIS_Y}),
    @MouseMoveInputMapping(action = ScrollCameraAdapter.MOUSE_MOVE_Y,axis = {MouseInput.AXIS_Y}, negate = true)    
})
@MouseButtonInputMapping(action=ScrollCameraAdapter.MOUSE_WHEEL_CLICK, buttons={MouseInput.BUTTON_MIDDLE})
public class ScrollCameraAdapter extends Model{
	public static final String MOUSE_MOVE_X="mouseMoveX";
	public static final String MOUSE_MOVE_Y="mouseMoveY";
	public static final String MOUSE_WHEEL_POSITIVE="mouseWheel+";
	public static final String MOUSE_WHEEL_NEGATIVE="mouseWheel-";
	public static final String MOUSE_WHEEL_CLICK="mouseWheelClick";
	
    @CamNode(name="ScrollCamera")
    private CameraNode camNode;
    
    private int offset=30;
    private int speed=20;
    private int rotateSpeed=20;

	private int moveX=0;
    private int moveZ=0;
    
    private boolean wheelClick;
    
    private Vector3f tmp=new Vector3f();

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
    	if(!wheelClick){
	        if(EntityManager.getInputManager().getCursorPosition().x<offset){
	            moveX=speed;
	        }else if(EntityManager.getInputManager().getCursorPosition().x>EntityManager.getGame().getContext().getSettings().getWidth()-offset){
	            moveX=-speed;
	        }else{
	            moveX=0;
	        }
    	}else{
    		if(valor>0){
    			rotate(0, rotateSpeed*tpf, 0);
    		}else if(valor<0){
    			rotate(0, -rotateSpeed*tpf, 0);
    		}
    	}
    }
    
    @Input(action = MOUSE_MOVE_Y)
    public void mouseMoveY(float valor, float tpf){
        if(EntityManager.getInputManager().getCursorPosition().y<offset){
            moveZ=-speed;
        }else if(EntityManager.getInputManager().getCursorPosition().y>EntityManager.getGame().getContext().getSettings().getHeight()-offset){
            moveZ=speed;
        }else{
            moveZ=0;
        }
    }
    
    @Input(action = MOUSE_WHEEL_POSITIVE)
    public void mouseWheel(float valor, float tpf){
        move(0,5*valor,-5*valor);
    }
    
    @Input(action = MOUSE_WHEEL_NEGATIVE)
    public void mouseWheelNeg(float valor, float tpf){
        move(0,-5*valor,5*valor);
    }
    
    @Input(action = MOUSE_WHEEL_CLICK)
    public void mouseWheelClick(boolean valor, float tpf){
    	wheelClick=valor;
    }
    
    @OnUpdate
    private void update(float tpf){
    	tmp.set(0,0,0);
    	
    	Vector3f modelLeftDir = EntityManager.getCamera().getRotation().mult(Vector3f.UNIT_X);
        Vector3f modelForwardDir = EntityManager.getCamera().getRotation().mult(new Vector3f(0,1,1));
        
        tmp.addLocal(modelLeftDir.mult(moveX*tpf));
        tmp.addLocal(modelForwardDir.mult(moveZ*tpf));
        
        move(tmp);
    	/*
        if(moveX!=0){
            move(moveX*tpf, 0, 0);
        }
        if(moveZ!=0){
            move(0,0,-moveZ*tpf);
        }
        */
    }

    @Override
    public void onDettach(IEntity parent) throws Exception {
        
    }       
    
	public void setValues(ScrollCameraNode anot){
		setSpeed(anot.speed());
		setOffset(anot.offset());
		setRotateSpeed(anot.rotateSpeed());
	}
    
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
 
    
    public int getRotateSpeed() {
		return rotateSpeed;
	}

	public void setRotateSpeed(int rotateSpeed) {
		this.rotateSpeed = rotateSpeed;
	}

}

