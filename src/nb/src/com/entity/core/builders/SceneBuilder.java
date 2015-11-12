package com.entity.core.builders;

import com.entity.anot.Instance;
import com.entity.anot.RayPick;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.RunGLThread;
import com.entity.core.EntityManager;
import com.entity.core.Injector;
import com.entity.core.injectors.CameraInjector;
import com.entity.core.injectors.EntityInjector;
import com.entity.core.injectors.FieldInjector;
import com.entity.core.injectors.InputInjector;
import com.entity.core.injectors.LightInjector;
import com.entity.core.injectors.MessageListenerInjector;
import com.entity.core.injectors.ScrollCameraInjector;
import com.entity.core.injectors.SkyInjector;
import com.entity.core.injectors.TerrainInjector;
import com.entity.core.injectors.TriggerInjector;
import com.entity.core.injectors.UpdateInjector;
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
		addInjector(new FieldInjector<Scene>());
		addInjector(new MessageListenerInjector<Scene>());
		addInjector(new TriggerInjector<Scene>());
		addInjector(new SkyInjector<Scene>());
        addInjector(new CameraInjector<Scene>());
        addInjector(new ScrollCameraInjector<Scene>());
        addInjector(new TerrainInjector<Scene>());
                
                Class<? extends Injector>[] customInjectors=EntityManager.getSceneCustomInjectors();
		if(customInjectors!=null){
			for(Class<? extends Injector> cInj:customInjectors){
				addInjector(cInj.newInstance());
			}
		}
	}
	
	
	@Override
	public void loadField(Class<Scene> cls, Field f) throws Exception {
		System.out.println(f.getName());
	}
	
	private void buildTrigger(Class<Scene> cls, Field f) throws Exception{

	}

	@Override
	public void loadMethod(Class<Scene> c, Method m) throws Exception {
		if(!mustEnhance){
                    if(m.isAnnotationPresent(RunGLThread.class)){
                            mustEnhance=true;
                    }else if(m.isAnnotationPresent(Instance.class)){
                        mustEnhance=true;
                    }else if(m.isAnnotationPresent(RayPick.class)){
                        mustEnhance=true;
                    }
                }
	}


	@Override
	public void injectInstance(Scene e) throws Exception {
            super.injectInstance(e);
            e.setNode();                
	}


	@Override
	public boolean isMustEnhance() {
		return mustEnhance;
	}




}
