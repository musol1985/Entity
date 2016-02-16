package com.entity.core.interceptors;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.google.dexmaker.stock.ProxyBuilder;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public abstract class BaseMethodInterceptor implements MethodInterceptor, InvocationHandler {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return interceptMethod(proxy, method, null, args);
	}

	@Override
	public Object intercept(Object obj, Method m, Object[] args,
			MethodProxy mp) throws Throwable {
		return interceptMethod(obj, m, mp, args);
	}

	
	public abstract Object interceptMethod(Object obj, Method m, MethodProxy mp, Object[] args)throws Throwable;
	
	public Object callSuper(Object obj, Method m, MethodProxy mp, Object[] args)throws Throwable{
		if(mp!=null){
			return mp.invokeSuper(obj, args);
		}else{
			return ProxyBuilder.callSuper(obj, m, args);
		}		
	}
}
