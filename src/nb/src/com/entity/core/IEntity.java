package com.entity.core;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public interface IEntity{
	public void onAttachToParent(IEntity parent)throws Exception;
	public Node getNode();
	public void attachToParent(IEntity parent)throws Exception;
	public void dettach()throws Exception;
	public void onDettach(IEntity parent)throws Exception;
    public void onInstance(IBuilder builder, Object[] params);
    public void onInstanceCache(IBuilder builder, Object[] params)throws Exception;
    public void onPreInject(IBuilder builder, Object[] params)throws Exception;
    public void setBuilder(IBuilder builder);
    public void attachChilFromInjector(Spatial s);
}
