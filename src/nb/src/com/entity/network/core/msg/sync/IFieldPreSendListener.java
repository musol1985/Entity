package com.entity.network.core.msg.sync;

public interface IFieldPreSendListener {
	public void onPreSend(String fieldName, Object value);
}
