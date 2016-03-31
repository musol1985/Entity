package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.adapters.TaskAdapter;
import com.entity.anot.Task;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.BaseInjector;
import com.entity.core.injectors.ListBeanInjector;

public class TaskInjector<T  extends IEntity>  extends ListBeanInjector<AnnotationFieldBean<Task>, T>{
	

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(Task.class,f)){
			beans.add(new AnnotationFieldBean<Task>(f, Task.class));
		}
	}


	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(AnnotationFieldBean<Task> bean:beans){ 
			TaskAdapter task=(TaskAdapter) EntityManager.instanceGeneric(bean.getField().getType());
			bean.getField().set(e, task);   
			
			task.onCreate(e);
			
			task.getScene().getTasks().scheduleAtFixedRate(task, bean.getAnnot().delay(), bean.getAnnot().period(), bean.getAnnot().unit());
		}
	}
	
    @Override
    public int compareTo(BaseInjector t) {
        return 2;
    }
}
