package com.entity.network.core.items;

import com.entity.network.core.listeners.InGameServerMessageListener;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.service.NetWorldService;

public interface IWorldInGameScene <W extends NetWorld,  S extends NetWorldService>{
	public abstract W getWorld();
	public abstract S getService();
}
