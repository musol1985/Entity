package com.entity.core.items.interceptors;

import java.lang.reflect.Method;

import com.entity.anot.Instance;
import com.entity.anot.RayPick;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.core.interceptors.RayPickInterceptor;
import com.entity.core.items.BatchModel;
import com.entity.core.items.Model;
import com.entity.core.items.ModelBase;

import net.sf.cglib.proxy.MethodProxy;

public class EntityMethodInterceptor extends ThreadsMethodInterceptor{

	@Override
	public Object interceptMethod(Object obj, Method method, MethodProxy mp, Object[] args) throws Throwable {
		if(EntityManager.isAnnotationPresent(RayPick.class,method)){
			return RayPickInterceptor.rayPick(obj, method, args, mp, this);
		}else if(EntityManager.isAnnotationPresent(Instance.class,method)){
            IEntity instance=null;
            if(args.length>1){
            	Object[] params=new Object[args.length-1];
            	for(int i=1;i<args.length;i++){
    				params[i-1]=args[i];
    			}
            	instance=(IEntity)EntityManager.instanceGeneric(method.getParameterTypes()[0], params);            	
            }else{
            	instance=(IEntity)EntityManager.instanceGeneric(method.getParameterTypes()[0]);            	
            }
                                 
            Instance anot=EntityManager.getAnnotation(Instance.class,method);
            if(Instance.THIS.equals(anot.attachTo())){
                IEntity e=(IEntity)obj;
                if(e instanceof BatchModel){
                    ((BatchModel)e).attachEntity((Model) instance);
                }else{
                    instance.attachToParent(e);
                }
            }else if(!anot.attachTo().isEmpty()){
                IEntity e=(IEntity)obj.getClass().getField(anot.attachTo()).get(obj);
                if(e instanceof BatchModel){
                    ((BatchModel)e).attachEntity((Model) instance);
                }else{
                    instance.attachToParent(e);
                }
            }
            args[0]=instance;
            return callSuper(obj, method, mp, args);
        }else{
            return super.interceptMethod(obj, method, mp, args);
		}
	}
}
