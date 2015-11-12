package com.entity.core;


public interface InjectorAttachable<T> {
	public <G extends EntityGame> void onAttach(G app, T instance)throws Exception;	
	public <G extends EntityGame> void onDettach(G app, T instance)throws Exception;
}
