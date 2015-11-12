package com.entity.core;

public interface IBuilder<T extends IEntity> extends Injector<T>{
	public void onCreate(Class<T> c)throws Exception;
	public void loadInjectors(Class<T> c)throws Exception;
	public boolean isMustEnhance();
	public void injectInstance(T e)throws Exception;
	public <T extends Injector> T getInjector(Class<T> injector);
	public void onAttachInstance(T e);
	public void onDettachInstance(T e);
}
