package com.entity.network.core.items;

import com.entity.core.IEntity;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.service.NetWorldService;
import com.entity.network.core.tasks.NetWorldPersistTask;

public interface IWorldInGameScene <W extends NetWorld,  S extends NetWorldService> extends IEntity{
	public abstract W getWorld();
	public abstract S getService();
	public abstract NetWorldPersistTask getPersistTask();
}
