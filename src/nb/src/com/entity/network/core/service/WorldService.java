package com.entity.network.core.service;

import java.util.Map.Entry;
import java.util.logging.Logger;

import com.entity.core.EntityManager;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.bean.NetWorldCell;
import com.entity.utils.Vector2;

public abstract class WorldService<T extends NetWorld<P, ? extends WorldService, C>,P extends NetPlayer, C extends NetWorldCell> {
	protected static final Logger log = Logger.getLogger(WorldService.class.getName());
	
	private T world;
	
	public WorldService(T world){
		this.world=world;
	}
	
	public boolean isAllPlayersReady(){
		for(Entry<String,? extends NetPlayer> p:world.getPlayers().entrySet()){
			if(!p.getValue().isReady())
				return false;
		}
			
		return true;
	}
	
	
	/**
	 * Get a cell by ID
	 * 
	 * Can override isCellInLimits and canCreateCell
	 * 
	 * @param id
	 * @return cell from(cache->Fs->Create) or null if id is not in limits
	 */
	public C getCellById(Vector2 cellId){
		C cell=null;
		if(isCellInLimits(cellId)){
			log.fine("getcellById from cache->"+cellId);
			cell=getCellFromCache(cellId);
			
			if(cell==null){
				log.fine("getcellById from fs->"+cellId);
				cell=getCellFromFS(cellId);
				
				if(canCreateCell() && cell==null){
					log.fine("The cell "+cellId+" isn't in cache and fs. It has to be created.");
					cell=createNewCell(cellId);
				}
			}
		}
		
		return cell;
		
	}
	
	private C getCellFromFS(Vector2 cellId){
		return (C)EntityManager.loadPersistable(world.getCachePath()+cellId+".cache");		
	}
	
	private C getCellFromCache(Vector2 cellId){
		return world.getCells().get(cellId);
	}
	
	private void saveCellFS(C cell){
		EntityManager.savePersistable(world.getCachePath()+cell.getId()+".cache", cell);		
	}
	
	public abstract C createNewCell(Vector2 cellId);
	public abstract P createNewPlayer(String name);
	
	public boolean isCellInLimits(Vector2 celldId){
		return true;
	}
	
	/**
	 * By default, the new cell can only be created by the server
	 * @return
	 */
	public boolean canCreateCell(){
		return EntityManager.getGame().getNet().isNetServerGame();
	}
	
	/**
	 * If overrides, it must call super
	 * @param cell
	 */
	public void onCellPopCache(C cell){
		saveCellFS(cell);
	}
	
	
}
