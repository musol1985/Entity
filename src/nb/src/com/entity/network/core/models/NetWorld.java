package com.entity.network.core.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.entity.anot.Persistable;
import com.entity.anot.entities.ModelEntity;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.items.Model;
import com.entity.network.core.beans.CellId;
import com.entity.network.core.beans.CellViewQuad;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.utils.Vector2;
import java.util.List;

@ModelEntity
public abstract class NetWorld<T extends NetWorldDAO, C extends NetWorldCell, P extends NetPlayer> extends Model{
	public static final int CELLS_CACHE_SIZE = 20;
	
	private static float HASH_TABLE_LOAD_FACTOR=0.75f;
	private static int HASH_TABLE_CAPACITY = (int) Math.ceil(CELLS_CACHE_SIZE / HASH_TABLE_LOAD_FACTOR) + 1;
	
	public T dao;
	public LinkedHashMap<Vector2, C> cellsCache;		
    public boolean temporal=false;
    
    //Only will load when injecting
    @Persistable(fileName="cells.idx", newOnNull=true, onNewSave=true)
    public HashMap<Vector2, CellId> cellsIndex;
    
    public List<P> players;
    
    public CellViewQuad view;


	@SuppressWarnings("unchecked")
	@Override
	public void onInstance(IBuilder builder, Object[] params) {
		dao=(T) EntityManager.getGame().getNet().getWorldService().getWorldDAO();
		EntityManager.getGame().getNet().getWorldService().setWorld(this);
		cellsCache=new LinkedHashMap<Vector2, C>(HASH_TABLE_CAPACITY, HASH_TABLE_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Vector2, C> eldest) {
                boolean pop=this.size() > CELLS_CACHE_SIZE;
                if(pop){
                	EntityManager.getGame().getNet().getWorldService().onCellPopCache(eldest.getValue());                    
                }
                return pop;
            }
		};
		super.onInstance(builder, params);
	}

	public T getDao() {
		return dao;
	}

	public void setDao(T dao) {
		this.dao = dao;
	}

	public LinkedHashMap<Vector2, C> getCells() {
		return cellsCache;
	}
	
	
	
	public HashMap<Vector2, CellId> getCellsIndex() {
		return cellsIndex;
	}

	public void setCellsIndex(HashMap<Vector2, CellId> cellsIndex) {
		this.cellsIndex = cellsIndex;
	}

	public int getVirtualCellSize(){
		return getCellSize()/2;
	}
	
	
	
	public boolean isTemporal() {
		return temporal;
	}
        
        public void setTemporal(boolean temporal){
            this.temporal=temporal;
        }

	public abstract int getCellSize();

	public CellViewQuad getView() {
		return view;
	}

	public void setView(CellViewQuad view) {
		this.view = view;
	}

	public List<P> getPlayers() {
		return players;
	}

	public void setPlayers(List<P> players) {
		this.players = players;
	}

}
