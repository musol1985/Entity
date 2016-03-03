package com.entity.core.interceptors;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import net.sf.cglib.proxy.MethodProxy;

import com.entity.core.EntityManager;

public class OnExecutorInterceptor {
	
	protected static final Logger log = Logger.getLogger(OnExecutorInterceptor.class.getName());

	@SuppressWarnings({ "unchecked"})
	public static Object onExecutor(final Object obj, final Method m,  final MethodProxy mp, final BaseMethodInterceptor mi, final Object...args)throws Exception, Throwable{
		Callable call=new Callable() {
			@Override
			public Object call() throws Exception {
				try {
					return mi.callSuper(obj, m, mp, args);
				} catch (Throwable e) {
					log.severe("Error onExecutor");
					e.printStackTrace();					
				}
				return null;
			}
		};
		
		EntityManager.getGame().getExecutor().submit(call);
		return call;
	}
	
}
