package com.entity.core.injectors;

import com.entity.anot.components.model.MaterialComponent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.bean.MaterialBean;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.entity.core.items.Model;

public class MaterialInjector<T extends IEntity>  extends BaseInjector<T>{
	
	private List<MaterialBean> materials=new ArrayList<MaterialBean>();

	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(MaterialBean m:materials){
                    System.out.println("-------------------------------------------------------->MaterialInjection");
			m.onLoad((Model)e);
		}
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(MaterialComponent.class)){
			f.setAccessible(true);
			materials.add(new MaterialBean(f, c, f.getAnnotation(MaterialComponent.class)));
		}
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return materials.size()>0;
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
        @Override
        public int compareTo(BaseInjector t) {
            return 1;
        }
}
