package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.components.model.MaterialComponent;
import com.entity.bean.custom.MaterialBean;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.BaseInjector;
import com.entity.core.injectors.ListBeanInjector;
import com.entity.core.items.Model;

public class MaterialInjector<T extends IEntity>  extends ListBeanInjector<MaterialBean, T>{
	
	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(MaterialComponent.class)){
			beans.add(new MaterialBean(f, c, MaterialComponent.class));
		}
	}

	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(MaterialBean m:beans){                    
			m.onLoad((Model)e);
		}
	}

	
    @Override
    public int compareTo(BaseInjector t) {
        return 1;
    }
}
