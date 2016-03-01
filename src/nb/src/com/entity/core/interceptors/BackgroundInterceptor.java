package com.entity.core.interceptors;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.entity.anot.OnBackground;
import com.entity.anot.RayPick;
import com.entity.core.EntityManager;

import net.sf.cglib.proxy.MethodProxy;

public class BackgroundInterceptor {
	
	protected static final Logger log = Logger.getLogger(BackgroundInterceptor.class.getName());

	public static Object onBackground(final Object obj, final Method m,  final MethodProxy mp, final BaseMethodInterceptor mi, final Object...args)throws Exception, Throwable{
		OnBackground anot=EntityManager.getAnnotation(OnBackground.class,m);
		
		Callable<Object> call=new Callable<Object>() {
	        public Object call() throws Exception {
	            try {
					return mi.callSuper(obj, m, mp, args);
				} catch (Throwable e) {
					log.severe("Error onBackground");
					e.printStackTrace();					
				}
	            return null;
	        }
	    };
	    
	    if(anot.timeout()>0){
	    	return EntityManager.getGame().enqueue(call).get(anot.timeout(), TimeUnit.MILLISECONDS);
	    }else{
	    	return EntityManager.getGame().enqueue(call).get();
	    }
	}
	
}
