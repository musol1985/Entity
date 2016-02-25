package com.entity.network.core.models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.items.Model;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.utils.Vector2;

public class NetWorld<T extends NetWorldDAO, C extends NetWorldCellDAO> extends Model{
	public static final int CELLS_SIZE = 10;
	private static float HASH_TABLE_LOAD_FACTOR=0.75f;
	private static int HASH_TABLE_CAPACITY = (int) Math.ceil(CELLS_SIZE / HASH_TABLE_LOAD_FACTOR) + 1;
	
	public T dao;
	public LinkedHashMap<Vector2, C> cells;


	@Override
	public void onInstance(IBuilder builder) {
		dao=(T) EntityManager.getGame().getNet().getWorldService().getWorldDAO();
		cells=new LinkedHashMap<Vector2, C>(HASH_TABLE_CAPACITY, HASH_TABLE_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Vector2, C> eldest) {
                boolean pop=this.size() > CELLS_SIZE;
                if(pop){
                	EntityManager.getGame().getNet().getWorldService().onCellPopCache(eldest.getValue());                    
                }
                return pop;
            }
		};
		super.onInstance(builder);
	}

	public T getDao() {
		return dao;
	}

	public void setDao(T dao) {
		this.dao = dao;
	}

	public LinkedHashMap<Vector2, C> getCells() {
		return cells;
	}
	
	
}
