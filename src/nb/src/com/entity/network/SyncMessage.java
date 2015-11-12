package com.entity.network;

import com.jme3.network.Message;

public abstract class SyncMessage implements Message, Comparable<SyncMessage>{
	public abstract SyncMessage cloneMessage();
}
