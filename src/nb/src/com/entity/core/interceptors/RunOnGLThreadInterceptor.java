package com.entity.core.interceptors;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import net.sf.cglib.proxy.MethodProxy;

import com.entity.core.EntityManager;

public class RunOnGLThreadInterceptor {
	
	protected static final Logger log = Logger.getLogger(RunOnGLThreadInterceptor.class.getName());

	public static Object runOnGL(final Object obj, final Method m,  final MethodProxy mp, final BaseMethodInterceptor mi, final Object...args)throws Exception, Throwable{
		
		Callable<Object> call=new Callable<Object>() {
	        public Object call() throws Exception {
	            try {
					return mi.callSuper(obj, m, mp, args);
				} catch (Throwable e) {
					log.severe("Error runOnGLThread");
					e.printStackTrace();					
				}
	            return null;
	        }
	    };

	    return EntityManager.getGame().enqueue(call);
	}
	
}
