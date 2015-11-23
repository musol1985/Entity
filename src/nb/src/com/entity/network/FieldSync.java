package com.entity.network;

import com.entity.anot.network.NetSync;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.items.NetworkModel;


public class FieldSync<T extends SyncMessage> {
	private SyncMessage<T> msg;
	private AnnotationFieldBean<NetSync> bean;

	private long lastSend;
	private NetworkModel entity;
	
	public FieldSync(AnnotationFieldBean<NetSync> bean, NetworkModel model) throws Exception{
		this.entity=model;
				
		T field=(T) bean.getField().get(model);
		this.msg = new SyncMessage<T>(getID(), field);	
	}
	
	public void onMessage(T msg)throws Exception{
		bean.getField().set(entity, msg.getField());
		
		if(entity instanceof IFieldUpdateListener){
			((IFieldUpdateListener) entity).onFieldUpdate(bean.getField().getName(), msg.getField());
		}
	}
	
	public String getID(){
		return getID(entity,bean);
	}
	
	public static String getID(NetworkModel model, AnnotationFieldBean<NetSync> bean){
		return model.getNetID()+bean.getField().getName();
	}


	public SyncMessage<T> getMsg() {
		return msg;
	}



	public int getTimeout() {
		return bean.getAnnot().timeout();
	}

	public long getLastSend() {
		return lastSend;
	}
	
	public NetworkModel getEntity() {
		return entity;
	}

	public void sent(){
		lastSend=System.currentTimeMillis();
	}
	
	public boolean mustSend(){
		return System.currentTimeMillis()-getLastSend()>getTimeout();
	}

}
