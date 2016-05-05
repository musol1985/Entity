package com.entity.core.injectors.field;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.anot.Entity;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;
import com.entity.core.items.ModelBase;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class EntityInjector<T  extends IEntity>  extends ListBeanInjector<AnnotationFieldBean<Entity>, T>{
	

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(Entity.class,f)){
			beans.add(new AnnotationFieldBean<Entity>(f, Entity.class));
		}
	}


	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(AnnotationFieldBean<Entity> bean:beans){ 
			Object conditional=conditionalInject(bean, e, params);
			boolean inject=conditional instanceof Boolean && ((Boolean)conditional==true);
			if(inject || conditional instanceof Class || conditional instanceof IEntity){
				IEntity entity=null;
				boolean doOnInstance=true;
				
				if(inject){
					entity=(IEntity) EntityManager.instanceGeneric(bean.getField().getType());
				}else if(conditional instanceof IEntity){
					entity=(IEntity)conditional;
					doOnInstance=false;
				}else{
					entity=(IEntity) EntityManager.instanceGeneric((Class)conditional);
				}
				
				
				if(!bean.getAnnot().callOnInject().isEmpty()){
					Method m=entity.getClass().getMethod(bean.getAnnot().callOnInject());
					if(m!=null){
						m.invoke(e, new Object[]{});
					}else{
						log.warning("@Entity.callOnInject method: "+bean.getAnnot().callOnInject()+" doesn't exists in class "+e.getClass().getName());
					}
				}
				if(doOnInstance)
					entity.onInstance(builder, params);
				
				bean.getField().set(e, entity);   						
				
				if(!bean.getAnnot().name().isEmpty())
					entity.getNode().setName(bean.getAnnot().name());
				
				if(bean.getAnnot().substituteNode().isEmpty()){
					if(bean.getAnnot().attach())
						entity.attachToParent((IEntity) e);			
				}else{
					Node n=(Node)e.getNode().getChild(bean.getAnnot().substituteNode());
					if(n!=null){
						Vector3f pos=n.getLocalTranslation();
						Quaternion rot=n.getLocalRotation();
						
						if(n.getParent() instanceof ModelBase){
							ModelBase parent=(ModelBase)n.getParent();
							parent.detachChild(n);
							
							parent.setLocalTranslation(pos);
							parent.setLocalRotation(rot);
							
							entity.attachToParent(parent);		
						}else{
							log.warning("@Entity.substituteNode the node: "+bean.getAnnot().substituteNode()+" has a parent that is not an ModelBase "+e.getClass().getName());
						}
					}else{
						log.warning("@Entity.substituteNode method: "+bean.getAnnot().substituteNode()+" doesn't exists in class "+e.getClass().getName());
					}					
				}
			}
		}
	}
	
	private Object conditionalInject(AnnotationFieldBean<Entity> bean, T e, Object[] params)throws Exception{
		if(!bean.getAnnot().conditional().isEmpty()){
			Class[] pTypes=null;
			
			if(bean.getAnnot().conditionalIncludeFieldName()){
				Object[] pTmp=new Object[params.length+1];
				for(int i=0;i<params.length;i++){
					pTmp[i]=params[i];
				}
				pTmp[params.length]=bean.getField().getName();
				params=pTmp;
			}
			
			pTypes=getParams(params);
			Method m=e.getClass().getDeclaredMethod(bean.getAnnot().conditional(), pTypes);
			if(m!=null){
				return m.invoke(e, params);                           
			}else{
				log.warning("@Entity.conditional method: "+bean.getAnnot().callOnInject()+" doesn't exists in class "+e.getClass().getName());
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
