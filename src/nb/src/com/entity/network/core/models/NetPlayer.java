package com.entity.network.core.models;

import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.items.Model;
import com.entity.network.core.dao.NetPlayerDAO;

public class NetPlayer<T extends NetPlayerDAO> extends Model{

	public T dao;


	@Override
	public void onInstance(IBuilder builder) {
		dao=(T) EntityManager.getGame().getNet().getPlayer();
		super.onInstance(builder);
	}

	public T getDao() {
		return dao;
	}

	public void setDao(T dao) {
		this.dao = dao;
	}
	
	
}
