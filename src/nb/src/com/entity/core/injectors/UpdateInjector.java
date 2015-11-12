package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.adapters.ControlAdapter;
import com.entity.anot.OnUpdate;
import com.entity.core.EntityGame;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.entity.core.items.Model;
import com.entity.core.items.Scene;
import com.jme3.scene.Node;

public class UpdateInjector<T extends IEntity>  extends BaseInjector<T>{
	private Method update;


	@Override
	public void onInstance(final T item, IBuilder builder) throws Exception {
		if(update!=null){
			Node n=null;
			if(item instanceof Scene){
				//TODO
			}else if(item instanceof Model){
				n=(Node) item;
			}
			if(n!=null){
				n.addControl(new ControlAdapter() {
					@Override
					public void update(float tpf) {
						try{
							update.invoke(item, tpf);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {

	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {
		if(m.isAnnotationPresent(OnUpdate.class)){
                    m.setAccessible(true);
			update=m;
		}
	}

	@Override
	public boolean hasInjections() {
		return update!=null;
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
