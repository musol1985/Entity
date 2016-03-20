package com.entity.network.core.msg.sync;

import com.entity.core.EntityManager;
import com.entity.core.items.NetworkModel;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public abstract class NetMessage<T extends NetworkModel> extends AbstractMessage{
	public abstract void onSend(T model);
	public abstract void onReceive(T model);
	
	public void sync()throws Exception{
		EntityManager.getCurrentScene().getNetSync().forceSync(this);
	}
}
