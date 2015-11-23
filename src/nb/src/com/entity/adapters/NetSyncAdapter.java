package com.entity.adapters;

import java.util.HashMap;
import java.util.Map.Entry;

import com.entity.core.EntityManager;
import com.entity.network.FieldSync;
import com.entity.network.SyncMessage;
import com.jme3.network.MessageConnection;


public class NetSyncAdapter extends ControlAdapter{
	private HashMap<String, FieldSync> syncLocal=new HashMap<String, FieldSync>();
	private HashMap<String, FieldSync> syncExternal=new HashMap<String, FieldSync>();
	private boolean isClientNetwork;
	
	public NetSyncAdapter(){
		isClientNetwork=EntityManager.getCurrentScene().getApp().getNetworkClient()!=null;
	}
	
	
	public void update(float tpf) {
		for(Entry<String, FieldSync> field:syncLocal.entrySet()){
			if(field.getValue().mustSend()){
				if(isClientNetwork){
					EntityManager.getCurrentScene().getApp().getNetworkClient().send(field.getValue().getMsg());
				}else{
					EntityManager.getCurrentScene().getApp().getNetworkServer().broadcast(field.getValue().getMsg());
				}
			}
		}
	}
	
	public void addField(FieldSync field){
		syncExternal.put(field.getID(), field);
	}
	
	public void removeField(String fieldId){
		syncExternal.remove(fieldId);
		syncLocal.remove(fieldId);
	}
	
	public void controlField(String fieldId){
		FieldSync field=syncExternal.remove(fieldId);
		syncLocal.put(fieldId, field);
	}
	
	public void unControlField(String fieldId){
		FieldSync field=syncLocal.remove(fieldId);
		syncExternal.put(fieldId, field);
	}
	
	//Called from networkMessageListener
	public void onMessage(MessageConnection cnn, SyncMessage msg)throws Exception{
		FieldSync field=syncExternal.get(msg.getId());
		if(field==null)
			throw new Exception("Null fieldsync for "+msg.getId());
		
		field.onMessage(msg);
	}
}
