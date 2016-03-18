package com.entity.adapters;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;

import com.entity.anot.network.Broadcast;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.core.items.Scene;
import com.entity.network.core.msg.MsgSync;
import com.jme3.network.Filters;
import com.jme3.network.Message;
import com.jme3.network.MessageConnection;
import com.jme3.network.MessageListener;

public class NetworkMessageListener<T extends IEntity> implements MessageListener<MessageConnection>{
	protected static final Logger log = Logger.getLogger(NetworkMessageListener.class.getName());
	
	private HashMap<Class<? extends Message>, Method> methods=new HashMap<Class<? extends Message>, Method>();
	
	private T entity;
	private boolean ignoreSync;
	
	public NetworkMessageListener() {
		 for(Method m:getClass().getMethods()){
			 Class<? extends Message> parMsg=getMessageParamMethod(m);
			 if(parMsg!=null){
				methods.put(parMsg, m);
			 }            
        }
	}
	
	
	
	public void setEntity(T entity) {
		this.entity = entity;
	}



	public T getEntity() {
		return entity;
	}



	public boolean isIgnoreSync() {
		return ignoreSync;
	}



	public void setIgnoreSync(boolean ignoreSync) {
		this.ignoreSync = ignoreSync;
	}



	private Class<? extends Message> getMessageParamMethod(Method m){
		for(Class param:m.getParameterTypes()){
			if(Message.class.isAssignableFrom(param))
				return param;
		}
		
		return null;
	}


	public void messageReceived(MessageConnection cnn, Message msg) {


        try {            
        	
        	if(msg instanceof MsgSync){
        		Scene s=EntityManager.getCurrentScene();
        		if(s!=null)
        			s.getNetSync().onMessage(cnn, (MsgSync) msg);
        		
        		if(ignoreSync){
        			return;        			
        		}else{
        			Method m=methods.get(((MsgSync)msg).getField().getClass());
        			if(m!=null){
        				m.invoke(this, ((MsgSync)msg).getField(), ((MsgSync)msg).getId().split("#")[1]);
        				broadCast(cnn, msg, true);
        			}else{
        				log.warning("No method implemented por message class "+msg+" in "+getClass().getName());
                        return;
        			}
        		}
    		}

        	
        	Method m=methods.get(msg.getClass());
                if(m==null){
                    log.warning("No method implemented por message class "+msg+" in "+getClass().getName());
                    return;
                }
                log.warning(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>On Net Message "+msg.getClass().getName());
        	Broadcast anot=EntityManager.getAnnotation(Broadcast.class,m);        	        	
        	
        	Boolean res=false;
        	
        	if(anot==null || anot.filter().length==0){
	        	if(m.getParameterTypes().length==0){
	        		res=(Boolean) m.invoke(this, new Object[0]);
	        	}else if(m.getParameterTypes().length==1){
	        		if(Message.class.isAssignableFrom(m.getParameterTypes()[0])){
	        			res=(Boolean) m.invoke(this, msg);
	        		}else{
	        			res=(Boolean) m.invoke(this, cnn);
	        		}
	        	}else if(m.getParameterTypes().length==2){
	        		if(Message.class.isAssignableFrom(m.getParameterTypes()[0])){
	        			res=(Boolean) m.invoke(this, msg, cnn);
	        		}else{
	        			res=(Boolean) m.invoke(this, cnn, msg);
	        		}
	        	}else{
	        		throw new Exception("Only 0,1,2 parameters on a method");
	        	}
	        	
	        	if(anot!=null){
	        		if(res==null || (res!=null && res==true)){
	        			broadCast(cnn, msg, anot.excludeSender());
	        		}        	
	        	}
        	}else{
        		if(passFilter(anot.filter(), msg)){
        			if(m.getParameterTypes().length==0){
        				res=(Boolean) m.invoke(this);
        			}else if(m.getParameterTypes().length==1){
        				res=(Boolean) m.invoke(this, cnn);
        			}else{
        				throw new Exception("Only 0 or 1 parameter on a method");
        			}
        			if(res==null || (res!=null && res==true)){
	        			broadCast(cnn, msg, anot.excludeSender());
	        		} 
        		}
        	}
        	
        } catch (Exception ex) {           
            ex.printStackTrace();
        }
	}
	
	private boolean passFilter(Class<? extends Message>[] filters, Message m){
		for(Class<? extends Message> filter:filters){
			if(filter==m.getClass())
				return true;
		}
		return false;
	}

	protected void broadCast(MessageConnection cnn, Message msg, boolean excludeSender)throws Exception{
		Scene<? extends EntityGame> scene=EntityManager.getCurrentScene();
		if(scene!=null){
			if(!excludeSender){
				scene.getApp().getNet().getServer().broadcast(msg);
			}else{
				scene.getApp().getNet().getServer().broadcast(Filters.notEqualTo(cnn), msg);
			}
		}else{
			throw new Exception("Can't broadcast a message. No scene loaded!");
		}
	}
}
