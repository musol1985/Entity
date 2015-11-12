package com.entity.network;


public class FieldSync<T extends SyncMessage> {
	private T msg;
	private int timeout;
	private long lastSend;
	private boolean client;
	private T lastMsg;
	
	
	public FieldSync(T msg, int timeout, boolean client) {
		this.msg = msg;
		this.timeout = timeout;
		this.client=client;
	}


	public T getMsg() {
		return msg;
	}


	public void setMsg(T msg) {
		this.msg = msg;
	}


	public int getTimeout() {
		return timeout;
	}


	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}


	public long getLastSend() {
		return lastSend;
	}


	public void setLastSend(long lastSend) {
		this.lastSend = lastSend;
	}


	public boolean isClient() {
		return client;
	}


	public void setClient(boolean client) {
		this.client = client;
	}


	public T getLastMsg() {
		return lastMsg;
	}


	public void setLastMsg(T lastMsg) {
		this.lastMsg = lastMsg;
	}
	
	
	public void sended(){
		lastSend=System.currentTimeMillis();
		
		lastMsg=(T) msg.cloneMessage();
	}
}
