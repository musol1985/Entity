/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import com.entity.anot.Conditional;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.core.Injector;

/**
 *
 * @author Edu
 */
public abstract class BaseInjector<T extends IEntity> implements Injector<T>,Comparable<BaseInjector>{
	
	protected static final Logger log = Logger.getLogger(BaseInjector.class.getName());
   
    @Override
    public int compareTo(BaseInjector t) {
        return -1;
    }
    
    protected boolean conditional(T e, Method m, Object[] params)throws Exception{
    	return conditional(e, m.getName(), EntityManager.getAnnotation(Conditional.class, m), params);
    }
    
    protected boolean conditional(T e, Field f, Object[] params)throws Exception{
    	return conditional(e, f.getName(), EntityManager.getAnnotation(Conditional.class, f), params);
    }
    
	
	protected boolean conditional(T e, String name, Conditional condition, Object[] params)throws Exception{
		if(condition!=null){
			if(condition.includeParams()){
				Class[] pTypes=null;
				
				if(condition.includeFieldName()){
					Object[] pTmp=new Object[params.length+1];
					for(int i=0;i<params.length;i++){
						pTmp[i]=params[i];
					}
					pTmp[params.length]=name;
					params=pTmp;
				}
				
				pTypes=getParams(params);
				
				Method me=e.getClass().getMethod(condition.method(), pTypes);
				if(me!=null){
					return (Boolean)me.invoke(e, params);                           
				}else{
					log.warning("@BaseInjector("+this+").conditional with params method: "+condition.method()+" doesn't exists in class "+e.getClass().getName());
				}
			}else{
				Method me=e.getClass().getMethod(condition.method());
				if(me!=null){
					return (Boolean)me.invoke(e);                           
				}else{
					log.warning("@BaseInjector("+this+").conditional without params method: "+condition.method()+" doesn't exists in class "+e.getClass().getName());
				}
			}
		}
		return true;
	}

	
	private Class[] getParams(Object[] params){
		Class[] pTypes=new Class[params.length];
		for(int i=0;i<params.length;i++){
			pTypes[i]=params[i].getClass();
		}
		return pTypes;
	}
	
	private Class[] getParamsAndFieldName(Object[] params, String fieldName){
		Class[] pTypes=new Class[params.length+1];
		for(int i=0;i<params.length;i++){
			pTypes[i]=params[i].getClass();
		}
		pTypes[params.length]=String.class;
		return pTypes;
	}
}
