package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.Entity;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;

public class EntityInjector<T  extends IEntity>  extends BaseInjector<T>{
	private List<Field> entities=new ArrayList<Field>();


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(Field f:entities){
			IEntity entity=(IEntity) EntityManager.instanceGeneric(f.getType());			
                        entity.onInstance(builder);
			f.set(e, entity);                        
			
			if(f.getAnnotation(Entity.class).attach())
				entity.attachToParent((IEntity) e);
		}
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(Entity.class)){
			f.setAccessible(true);
			entities.add(f);
		}
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return entities.size()>0;
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
