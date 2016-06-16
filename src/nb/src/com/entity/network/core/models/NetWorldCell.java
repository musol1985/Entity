package com.entity.network.core.models;

import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.items.Model;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.items.IWorldInGameScene;
import com.jme3.math.Vector3f;

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

	public void save(){		
		((IWorldInGameScene)EntityManager.getCurrentScene()).getService().onUpdateCell(this);		
	}
        
        public Vector3f localToWorld(Vector3f pos){
            return new Vector3f(dao.getId().id.x*getCELL_SIZE()+pos.x,pos.y, dao.getId().id.z*getCELL_SIZE()+pos.z);
        }
        
        public abstract int getCELL_SIZE();
}
