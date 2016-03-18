package com.entity.network.core.msg.sync;

import com.entity.anot.network.NetSync;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.items.NetworkModel;
import com.entity.network.core.msg.MsgSync;


public class FieldSync<T extends NetMessage> {
	private MsgSync<T> msg;
	private AnnotationFieldBean<NetSync> bean;

	private long lastSend;
	private NetworkModel entity;
	
	public FieldSync(AnnotationFieldBean<NetSync> bean, NetworkModel model) throws Exception{
		this.entity=model;
                this.bean=bean;
				
		T field=(T) bean.getField().get(model);   
                if(field==null){
                    field=(T)bean.getField().getType().newInstance();
                    bean.getField().set(model, field);
                }
		this.msg = new MsgSync<T>(getID(), field);	
	}
	
	public void onMessage(MsgSync<T> msg)throws Exception{
		bean.getField().set(entity, msg.getField());
		
		if(entity instanceof IFieldUpdateListener){
			((IFieldUpdateListener) entity).onFieldUpdate(bean.getField().getName(), msg.getField());
		}
		
		msg.getField().onReceive(entity);
	}
	
	public void preSend()throws Exception{
		if(entity instanceof IFieldPreSendListener){
			((IFieldPreSendListener) entity).onPreSend(bean.getField().getName(), msg.getField());
		}
		msg.getField().onSend(entity);
	}
	
	public String getID(){
		return getID(entity,bean);
	}
	
	public static String getID(NetworkModel model, AnnotationFieldBean<NetSync> bean){
		return model.getNetID()+"#"+bean.getField().getName();
	}


	public MsgSync<T> getMsg() {
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

	public void forceSend(){
		sent();
	}
}
