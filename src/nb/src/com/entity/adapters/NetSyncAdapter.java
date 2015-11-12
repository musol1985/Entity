package com.entity.adapters;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.network.NetSync;
import com.entity.core.EntityManager;
import com.entity.core.items.Model;
import com.entity.network.FieldSync;
import com.entity.network.SyncMessage;


public class NetSyncAdapter extends ControlAdapter{
	private List<FieldSync> fields=new ArrayList<FieldSync>();
	
	@Override
	public void update(float arg0) {
		for(FieldSync f:fields){
			if(System.currentTimeMillis()-f.getLastSend()>f.getTimeout()){
				if(isDiferentValue(f)){
					if(f.isClient()){
						EntityManager.getCurrentScene().getApp().getNetworkClient().send(f.getMsg());
					}else{
						EntityManager.getCurrentScene().getApp().getNetworkServer().broadcast(f.getMsg());
					}
				
					f.sended();
				}
			}
		}
	}
	
	public boolean isDiferentValue(FieldSync f){
		return (f.getLastMsg()!=null && f.getLastMsg()!=f.getMsg() && f.getLastMsg().compareTo(f.getMsg())!=0);
	}

	public void addField(Field f, Model e)throws Exception{
		NetSync anot=f.getAnnotation(NetSync.class);
		f.setAccessible(true);
		fields.add(new FieldSync((SyncMessage) f.get(e), anot.timeout(), EntityManager.getCurrentScene().getApp().getNetworkClient()!=null));
	}
}
