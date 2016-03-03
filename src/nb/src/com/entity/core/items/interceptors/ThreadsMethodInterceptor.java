package com.entity.core.items.interceptors;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

import com.entity.anot.OnExecutor;
import com.entity.anot.RunOnGLThread;
import com.entity.core.EntityManager;
import com.entity.core.interceptors.BaseMethodInterceptor;
import com.entity.core.interceptors.OnExecutorInterceptor;
import com.entity.core.interceptors.RunOnGLThreadInterceptor;

public class ThreadsMethodInterceptor extends BaseMethodInterceptor{

	@Override
	public Object interceptMethod(Object obj, Method method, MethodProxy mp, Object[] args) throws Throwable {
		if(EntityManager.isAnnotationPresent(OnExecutor.class,method)){
			return OnExecutorInterceptor.onExecutor(obj, method, mp, this, args);
        }else if(EntityManager.isAnnotationPresent(RunOnGLThread.class,method)){
			return RunOnGLThreadInterceptor.runOnGL(obj, method, mp, this, args);
        }else{
            return callSuper(obj, method, mp,  args);
		}
	}
}
