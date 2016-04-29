package com.entity.core.injectors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;

import com.entity.anot.components.input.ComposedKeyInput;
import com.entity.anot.components.input.ComposedMouseButtonInput;
import com.entity.anot.components.input.ComposedMouseMoveInputMapping;
import com.entity.anot.components.input.KeyInputMapping;
import com.entity.anot.components.input.MouseButtonInputMapping;
import com.entity.anot.components.input.MouseMoveInputMapping;
import com.entity.core.EntityGame;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.InjectorAttachable;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;

public class TriggerInjector<T extends IEntity>  extends BaseInjector<T> implements InjectorAttachable<T>{		
	
	private HashMap<String, Trigger[]> triggers=new HashMap<String, Trigger[]>();


	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
	
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return triggers.size()>0;
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance) {
		for(Entry<String, Trigger[]> e:triggers.entrySet()){
			log.info("Registering Mapping "+e.getKey()+" "+e.getValue());
			app.getInputManager().addMapping(e.getKey(), e.getValue());
		}
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance) {
		for(Entry<String, Trigger[]> e:triggers.entrySet()){
			app.getInputManager().deleteMapping(e.getKey());
		}
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		for(Annotation a:c.getAnnotations()){
                    if(a instanceof ComposedMouseMoveInputMapping){
                        for(Annotation multi:((ComposedMouseMoveInputMapping)a).inputs()){
                            mapAnnotation(c, multi);
                        }
                    }else if(a instanceof ComposedMouseButtonInput){
                        for(Annotation multi:((ComposedMouseButtonInput)a).inputs()){
                            mapAnnotation(c, multi);
                        }
                    }else if(a instanceof ComposedKeyInput){
                    	for(Annotation multi:((ComposedKeyInput)a).inputs()){
                    		mapAnnotation(c, multi);
                    	}
                    }else{
                        mapAnnotation(c, a);
                    }
		}
		
	}
        
        
        private void mapAnnotation(Class<T> c,Annotation a){
            if(a instanceof MouseButtonInputMapping){
                    MouseButtonInputMapping key=(MouseButtonInputMapping)a;

                    MouseButtonTrigger[] triggers=new MouseButtonTrigger[key.buttons().length];
                    for(int i=0;i<key.buttons().length;i++){
                    	log.info("Adding MouseButtonMapping: "+key.buttons()[i]);
                            triggers[i]=new MouseButtonTrigger(key.buttons()[i]);
                    }			
                    addTrigger(c.getName(), key.action(), triggers);
            }else if(a instanceof MouseMoveInputMapping){
                    MouseMoveInputMapping key=(MouseMoveInputMapping)a;

                    MouseAxisTrigger[] triggers=new MouseAxisTrigger[key.axis().length];
                    for(int i=0;i<key.axis().length;i++){
                    	log.info("Adding MouseAxisMapping: "+key.axis()[i]);
                        triggers[i]=new MouseAxisTrigger(key.axis()[i], key.negate());
                    }
                    addTrigger(c.getName(), key.action(), triggers);			
            }else if(a instanceof KeyInputMapping){			
                    KeyInputMapping key=(KeyInputMapping)a;

                    KeyTrigger[] triggers=new KeyTrigger[key.keys().length];
                    for(int i=0;i<key.keys().length;i++){
                            log.info("Adding keyInputMapping: "+key.keys()[i]);
                            triggers[i]=new KeyTrigger(key.keys()[i]);
                    }
                    addTrigger(c.getName(), key.action(), triggers);
            }
        }

	private void addTrigger(String className, String action, Trigger[] triggers){
            Trigger[] actual=this.triggers.get(action);
            if(actual!=null){
                this.triggers.put(className+"."+action, concat(actual, triggers));
            }else{
                this.triggers.put(className+"."+action, triggers);
            }
        }
        
        public Trigger[] concat(Trigger[] a, Trigger[] b) {
            int aLen = a.length;
            int bLen = b.length;
            Trigger[] c= new Trigger[aLen+bLen];
            System.arraycopy(a, 0, c, 0, aLen);
            System.arraycopy(b, 0, c, aLen, bLen);
            return c;
         }  
}
