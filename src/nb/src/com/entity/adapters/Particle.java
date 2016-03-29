/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.adapters;

import com.entity.adapters.listeners.IScrollCameraListener;
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
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Edu
 */
@ModelEntity
public class Particle extends Model{

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
        camNode.move(0,5*valor,-5*valor);
    }
    
    @Input(action = MOUSE_WHEEL_NEGATIVE)
    public void mouseWheelNeg(float valor, float tpf){
        camNode.move(0,-5*valor,5*valor);
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

        if(listener!=null && tmp.length()>0){
        	listener.onUpdate(this);
        }
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



	public IScrollCameraListener getListener() {
		return listener;
	}



	public void setListener(IScrollCameraListener listener) {
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
}

