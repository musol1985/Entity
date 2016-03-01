package com.entity.network.core.models;

import com.entity.anot.entities.ModelEntity;
import com.entity.core.IBuilder;
import com.entity.core.items.Model;
import com.entity.network.core.dao.NetWorldCellDAO;

@ModelEntity
public abstract class NetWorldCell<T extends NetWorldCellDAO> extends Model{

	public T dao;

	public T getDao() {
		return dao;
	}

	public void setDao(T dao) {
		this.dao = dao;
	}
	
	

	@Override
	public void onPreInject(IBuilder builder, Object[] params) throws Exception {
		if(params==null || params.length==0){
			log.severe("Creating the networldCellModel: No CellDAO in params!!!!");
			throw new RuntimeException("Creating the networldCellModel: No CellDAO in params!!!!");
		}else{
			dao=(T)params[0];
		}
		
		super.onPreInject(builder, params);
	}
	
}
