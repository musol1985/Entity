package com.entity.core.items;

import java.util.logging.Logger;

import com.entity.anot.BuilderDefinition;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.builders.ServiceBuilder;
import com.entity.core.items.interceptors.EntityMethodInterceptor;
import com.entity.core.items.interceptors.ThreadsMethodInterceptor;
import com.entity.network.core.beans.CellId;
import com.entity.network.core.beans.CellViewQuad;
import com.entity.network.core.builders.NetWorldServiceBuilder;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.models.NetWorldCell;
import com.entity.utils.Vector2;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

@BuilderDefinition(builderClass=ServiceBuilder.class, methodInterceptorClass=EntityMethodInterceptor.class)
public abstract class BaseService implements IEntity{
	protected static final Logger log = Logger.getLogger(BaseService.class.getName());


	@Override
	public void onAttachToParent(IEntity parent) throws Exception {
		
	}



	@Override
	public Node getNode() {
		return null;
	}



	@Override
	public void attachToParent(IEntity parent) throws Exception {
		
	}



	@Override
	public void dettach() throws Exception {
		
	}



	@Override
	public void onDettach(IEntity parent) throws Exception {
		
	}



	@Override
	public void onInstance(IBuilder builder, Object[] params) {
		
	}



	@Override
	public void onPreInject(IBuilder builder, Object[] params) throws Exception {
		
	}



	@Override
	public void setBuilder(IBuilder builder) {
		
	}



	@Override
	public void attachChilFromInjector(Spatial s) {
		
	}
	
	

}