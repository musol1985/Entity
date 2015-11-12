/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.adapters;

import com.entity.anot.CamNode;
import com.entity.anot.OnUpdate;
import com.entity.anot.components.input.ComposedMouseMoveInputMapping;
import com.entity.anot.components.input.Input;
import com.entity.anot.components.input.MouseMoveInputMapping;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.builders.ModelBuilder;
import com.entity.core.items.Model;
import com.jme3.input.MouseInput;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;

/**
 *
 * @author Edu
 */
@ModelEntity
@ComposedMouseMoveInputMapping(inputs = {
    @MouseMoveInputMapping(action = "mouseMove",axis = {MouseInput.AXIS_X}),
    @MouseMoveInputMapping(action = "mouseMove",axis = {MouseInput.AXIS_X}, negate = true),
    @MouseMoveInputMapping(action = "mouseWheel+",axis = {MouseInput.AXIS_WHEEL}, negate = true),
    @MouseMoveInputMapping(action = "mouseWheel-",axis = {MouseInput.AXIS_WHEEL}),
    @MouseMoveInputMapping(action = "mouseMoveY",axis = {MouseInput.AXIS_Y}),
    @MouseMoveInputMapping(action = "mouseMoveY",axis = {MouseInput.AXIS_Y}, negate = true),
})
public class ScrollCameraAdapter extends Model{
    @CamNode(name="ScrollCamera")
    private CameraNode camNode;
    
    private int offset=30;
    private int speed=20;
    
    private int moveX=0;
    private int moveZ=0;

    @Override
    public void onInstance(IBuilder builder) {
        EntityManager.getGame().getFlyByCamera().setMoveSpeed(100);    
        EntityManager.getGame().getFlyByCamera().setEnabled(false);
        
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camNode.setEnabled(true);
        
        attachChild(camNode);
        camNode.setLocalTranslation(0, 40, -40);
        camNode.setLocalRotation(new Quaternion().fromAngles(FastMath.HALF_PI/2, 0, 0));
    }


    
    @Input(action = "mouseMove")
    public void mouseMove(float valor, float tpf){
        if(EntityManager.getInputManager().getCursorPosition().x<offset){
            moveX=speed;
        }else if(EntityManager.getInputManager().getCursorPosition().x>EntityManager.getGame().getContext().getSettings().getWidth()-offset){
            moveX=-speed;
        }else{
            moveX=0;
        }
    }
    
    @Input(action = "mouseMoveY")
    public void mouseMoveY(float valor, float tpf){
        if(EntityManager.getInputManager().getCursorPosition().y<offset){
            moveZ=speed;
        }else if(EntityManager.getInputManager().getCursorPosition().y>EntityManager.getGame().getContext().getSettings().getHeight()-offset){
            moveZ=-speed;
        }else{
            moveZ=0;
        }
    }
    
    @Input(action = "mouseWheel+")
    public void mouseWheel(float valor, float tpf){
        move(0,5*valor,-5*valor);
    }
    
    @Input(action = "mouseWheel-")
    public void mouseWheelNeg(float valor, float tpf){
        move(0,-5*valor,5*valor);
    }
    
    @OnUpdate
    private void update(float tpf){
        if(moveX!=0){
            move(moveX*tpf, 0, 0);
        }
        if(moveZ!=0){
            move(0,0,-moveZ*tpf);
        }
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

    @Override
    public void onDettach(IEntity parent) throws Exception {
        
    }
    
    
    
}

