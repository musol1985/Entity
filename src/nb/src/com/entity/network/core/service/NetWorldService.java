package com.entity.network.core.service;

import java.util.Map.Entry;
import java.util.logging.Logger;

import com.entity.core.EntityManager;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.utils.Vector2;
import com.jme3.network.HostedConnection;

public abstract class NetWorldService<W extends NetWorld<D, C>, P extends NetPlayer<E>, C extends NetWorldCellDAO, D extends NetWorldDAO<E>, E extends NetPlayerDAO> {
	protected static final Logger log = Logger.getLogger(NetWorldService.class.getName());

	protected W world;
	protected P player;
	
	/**
	 * @CalledOnServer
	 * @return
	 */
	public boolean isAllPlayersReady(){
		for(Entry<String,? extends NetPlayerDAO> p:world.getDao().getPlayers().entrySet()){
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
			log.info("getcellById from cache->"+cellId);
			cell=getCellFromCache(cellId);
			
			if(cell==null){
				log.info("getcellById from fs->"+cellId);
				cell=getCellFromFS(cellId);
				
				if(canCreateCell() && cell==null){
					log.info("The cell "+cellId+" isn't in cache and fs. It has to be created.");
					cell=createNewCell(cellId);
				}
			}
		}
		
		return cell;
		
	}
	
	private C getCellFromFS(Vector2 cellId){
		return (C)EntityManager.loadPersistable(world.getDao().getCachePath()+cellId+".cache");		
	}
	
	private C getCellFromCache(Vector2 cellId){
		return world.getCells().get(cellId);
	}
	
	private void saveCellFS(C cell){
		EntityManager.savePersistable(world.getDao().getCachePath()+cell.getId()+".cache", cell);		
	}
	
	public abstract C createNewCell(Vector2 cellId);
	public abstract P createNewPlayer(String name);

	
	public boolean isCellInLimits(Vector2 cellId){
		return cellId.x<world.getDao().getMaxRealSize() && cellId.z<world.getDao().getMaxRealSize();
	}
	
	/**
	 * @CalledOnServer
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
	
	/**
	 * @CalledOnServer
	 * Preload the world when starting the game the first time
	 * Example: position player, initial values, seeds, etc.
	 */
	public abstract void preload();
	
	
	/**
	 * @CalledOnServer
	 * @param cnn
	 * @return
	 */
	public E getPlayerByConnection(HostedConnection cnn){
		for(E player:world.getDao().getPlayers().values()){
			if(player.getCnn()==cnn)
				return player;
		}
		return null;
	}

	public D getWorldDAO() {
		return world.getDao();
	}

	public void setWorldDAO(D world) {
		this.world.setDao(world);
	}


	public E getPlayerDAO() {
		return player.getDao();
	}


	public void setPlayerDAO(E playerDAO) {
		this.player.setDao(playerDAO);
	}
	
	
}
