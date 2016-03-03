package com.entity.core.items.interceptors;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

import com.entity.anot.Instance;
import com.entity.anot.RayPick;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.core.interceptors.RayPickInterceptor;

public class EntityMethodInterceptor extends ThreadsMethodInterceptor{

	@Override
	public Object interceptMethod(Object obj, Method method, MethodProxy mp, Object[] args) throws Throwable {
		if(EntityManager.isAnnotationPresent(RayPick.class,method)){
			return RayPickInterceptor.rayPick(obj, method, args, mp, this);
		}else if(EntityManager.isAnnotationPresent(Instance.class,method)){
            IEntity instance=(IEntity)EntityManager.instanceGeneric(method.getParameterTypes()[0]);
            Instance anot=EntityManager.getAnnotation(Instance.class,method);
            if(Instance.THIS.equals(anot.attachTo())){
                IEntity e=(IEntity)obj;
                instance.attachToParent(e);
            }else{
                IEntity e=(IEntity)obj.getClass().getField(anot.attachTo()).get(obj);
                instance.attachToParent(e);
            }
            return callSuper(obj, method, mp, new Object[]{instance});
        }else{
            return super.interceptMethod(obj, method, mp, args);
		}
	}
}
