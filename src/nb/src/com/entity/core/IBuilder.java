package com.entity.core;

import com.entity.core.interceptors.BaseMethodInterceptor;

public interface IBuilder<T extends IEntity> extends Injector<T>{
	public void onCreate(Class<T> c)throws Exception;
	public void loadInjectors(Class<T> c)throws Exception;
	public boolean isMustEnhance();
	public boolean isCache();
	public void injectInstance(T e)throws Exception;
	public <T extends Injector> T getInjector(Class<T> injector);
	public void onAttachInstance(T e);
	public void onDettachInstance(T e);
	public BaseMethodInterceptor getInterceptor();
	public void setInterceptor(BaseMethodInterceptor interceptor);
}
