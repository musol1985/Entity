package com.entity.core.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.Instance;
import com.entity.anot.RayPick;
import com.entity.anot.RunOnGLThread;
import com.entity.anot.network.ActivateNetSync;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.Injector;
import com.entity.core.injectors.ListBeanSingletonInjector;
import com.entity.core.injectors.TriggerInjector;
import com.entity.core.injectors.field.CameraInjector;
import com.entity.core.injectors.field.EffectInjector;
import com.entity.core.injectors.field.EntityInjector;
import com.entity.core.injectors.field.LightInjector;
import com.entity.core.injectors.field.ListInjector;
import com.entity.core.injectors.field.MapInjector;
import com.entity.core.injectors.field.NetworkListenerInjector;
import com.entity.core.injectors.field.PersistableInjector;
import com.entity.core.injectors.field.CamerasInjector;
import com.entity.core.injectors.field.SkyInjector;
import com.entity.core.injectors.field.TaskInjector;
import com.entity.core.injectors.field.TerrainInjector;
import com.entity.core.injectors.input.InputInjector;
import com.entity.core.injectors.method.UpdateInjector;
import com.entity.core.items.Model;
import com.entity.core.items.Scene;

public class SceneBuilder extends Builder<Scene>{
	private boolean mustEnhance;

	@Override
	public void loadInjectors(Class<Scene> c) throws Exception {
		addInjector(new UpdateInjector<Scene>());
		addInjector(new InputInjector<Scene>());
		addInjector(new LightInjector<Scene>());
		addInjector(new EntityInjector<Scene>());
		addInjector(new ListBeanSingletonInjector<Scene>());
		addInjector(new NetworkListenerInjector<Scene>());
		addInjector(new TriggerInjector<Scene>());
		addInjector(new SkyInjector<Scene>());
        addInjector(new CameraInjector<Scene>());
        addInjector(new CamerasInjector<Scene>());
        addInjector(new TerrainInjector<Scene>());
        addInjector(new EffectInjector<Scene>());
        addInjector(new ListInjector<Model>());
        addInjector(new MapInjector<Model>());
        addInjector(new PersistableInjector<Scene>());
        addInjector(new TaskInjector<Scene>());
                
                Class<? extends Injector>[] customInjectors=EntityManager.getSceneCustomInjectors();
		if(customInjectors!=null){
			for(Class<? extends Injector> cInj:customInjectors){
				addInjector(cInj.newInstance());
			}
		}
	}
	
	private void buildTrigger(Class<Scene> cls, Field f) throws Exception{

	}
	
	@Override
	public void loadField(Class<Scene> c, Field f) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadMethod(Class<Scene> c, Method m) throws Exception {
		if(!mustEnhance){
            if(EntityManager.isAnnotationPresent(RunOnGLThread.class,m)){
                    mustEnhance=true;
            }else if(EntityManager.isAnnotationPresent(Instance.class,m)){
                mustEnhance=true;
            }else if(EntityManager.isAnnotationPresent(RayPick.class,m)){
                mustEnhance=true;
            }
        }
	}


	@Override
	public void injectInstance(Scene e) throws Exception {
            super.injectInstance(e);
            e.setNode();  
            		
            if(e.getClass().isAnnotationPresent(ActivateNetSync.class)){
                    e.activateNetSync();
            }
	}


	@Override
	public boolean isMustEnhance() {
		return mustEnhance;
	}

	@Override
	public void onInstance(Scene item, IBuilder builder, Object[] params) throws Exception {
		item.setApp(EntityManager.getGame());
		
		super.onInstance(item, builder, params);
	}






}
