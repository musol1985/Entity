package com.entity.network.core.models;

import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.items.Model;
import com.entity.network.core.dao.NetPlayerDAO;

@ModelEntity
public class NetPlayer<T extends NetPlayerDAO> extends Model{
	private boolean remote;
	public T dao;


	@Override
	public void onInstance(IBuilder builder, Object[] params) {			
        if(EntityManager.getGame().getNet().getWorldService().getPlayer()==null){
        	dao=(T) EntityManager.getGame().getNet().getWorldService().getPlayerDAO();
            EntityManager.getGame().getNet().getWorldService().setPlayer(this);            
        }else if( EntityManager.getGame().getNet().getWorldService().getPlayer().dao.getId().equals(dao.getId())){
        	dao=(T) EntityManager.getGame().getNet().getWorldService().getPlayerDAO();
            EntityManager.getGame().getNet().getWorldService().setPlayer(this);
        }else if(params.length>0){
        	dao=(T) params[0];
        	remote=true;
        }
        
		super.onInstance(builder, params);
	}

	public T getDao() {
		return dao;
	}

	public void setDao(T dao) {
		this.dao = dao;
	}
	public boolean isRemote(){
		return remote;
	}
	
}
