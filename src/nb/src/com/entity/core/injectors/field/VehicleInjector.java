/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.components.model.VehicleComponent;
import com.entity.bean.custom.VehicleBean;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.BaseInjector;
import com.entity.core.injectors.ListBeanInjector;
import com.entity.core.items.ModelBase;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;


/**
 *
 * @author Edu
 */
public class VehicleInjector<T extends IEntity> extends ListBeanInjector<VehicleBean, T> implements InjectorAttachable<T>{

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {            
		if(EntityManager.isAnnotationPresent(VehicleComponent.class,f)){
			beans.add(new VehicleBean(c, f));
		}
	}

	@Override
	public void onInstance(T item, IBuilder builder, Object[] params) throws Exception {
		for(VehicleBean bean:beans){

			VehicleControl control=new VehicleControl(bean.getCollisionShape(item), bean.getAnnot().mass());
			
	        float stiffness = bean.getAnnot().stiffness();//200=f1 car
	        float compValue = bean.getAnnot().compValue(); //(should be lower than damp)
	        float dampValue = bean.getAnnot().dampValue();
	        control.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
	        control.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
	        control.setSuspensionStiffness(stiffness);
	        control.setMaxSuspensionForce(bean.getAnnot().maxSuspensionForce());
	        
	        bean.addWheels(control, (ModelBase)item);

			bean.getField().set(item, control);			
		}
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance)throws Exception {
		if(EntityManager.getGame().isPhysics())
			for(VehicleBean bean:beans){
				if(bean.getAnnot().attachWorld()){
					VehicleControl  body=(VehicleControl)bean.getField().get(instance);	
					if(bean.getComponentField()!=null){
						Object component=bean.getComponentField().get(instance);
						if(component instanceof IEntity){
							((IEntity)component).getNode().addControl(body);
						}else{
							((Spatial)component).addControl(body);
						}
					}else{
						instance.getNode().addControl(body);
					}                                
					EntityManager.getCurrentScene().getPhysics().add(body);
				}
			}
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance)throws Exception {
            if(EntityManager.getGame().isPhysics())
				for(VehicleBean bean:beans){
					if(bean.getAnnot().attachWorld()){
						PhysicsControl body=(PhysicsControl)bean.getField().get(instance);	
						if(bean.getComponentField()!=null){
		                    Object component=bean.getComponentField().get(instance);
							if(component instanceof IEntity){
								((IEntity)component).getNode().removeControl(body);
							}else{
								((Spatial)component).removeControl(body);
							}
						}else{
							instance.getNode().removeControl(body);
						}
						EntityManager.getCurrentScene().getPhysics().remove(body);
					}
				}
	}
	
	@Override
    public int compareTo(BaseInjector t) {
        return 1;
    }
}
