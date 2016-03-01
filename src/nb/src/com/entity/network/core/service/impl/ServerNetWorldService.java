package com.entity.network.core.service.impl;

import com.entity.anot.OnBackground;
import com.entity.core.EntityManager;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.models.NetWorldCell;
import com.entity.network.core.service.NetWorldService;
import com.entity.utils.Vector2;
import com.jme3.network.HostedConnection;
import com.jme3.network.NetworkClient;

public abstract class ServerNetWorldService<W extends NetWorld, P extends NetPlayer, C extends NetWorldCell, D extends NetWorldDAO<E>, E extends NetPlayerDAO, F extends NetWorldCellDAO> extends NetWorldService<W,P,C,D,E,F>{
	
	/**
	 * Create a new cellDAO(called when a player reuqests new Cell
	 * is usefull to set the objects(trees, terrain, etc)
	 * @param cellId
	 * @return
	 */
	public abstract F onNewCellDAO(Vector2 cellId);
	
	/**
	 * @CalledOnServer
	 * @return
	 */
	public boolean isAllPlayersReady(){
		for(Object p:world.getDao().getPlayers().values()){
			if(!((E)p).isReady())
				return false;
		}
			
		return true;
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
		for(Object player:world.getDao().getPlayers().values()){
			if(((E)player).getCnn()==cnn)
				return (E)player;
		}
		return null;
	}

	/**
	 * Get a cell by ID if not exist returns null
	 */
	@Override
	public C getCellById(Vector2 cellId) {
		C cell=null;
		if(isCellInLimits(cellId)){

			log.info("getcellById from cache->"+cellId);
			cell=getCellFromCache(cellId);			

			if(cell==null){
				log.info("getcellById check if it is in cell indexes->"+cellId);
				if(getCellIdFromIndexes(cellId)!=null){
					log.info("Cell "+cellId+" in indexes, loading from FS");
					cell=getCellFromFS(cellId);
				}else{
					log.info("Cell "+cellId+" isn't in indexes.");
				}
			}
			
			if(cell!=null){				
				log.info("The cell "+cellId+" already exist. Put on first place in cache.");
				world.cellsCache.remove(cellId);
				world.cellsCache.put(cellId, cell);
			}
		}
		return cell;
	}
	
	
	/**
	 * Create a new cell dao(background)
	 * @param cellId
	 * @return
	 */
	public boolean createCellDAO(Vector2 cellId){
		if(isCellInLimits(cellId)){
			log.info("The cell "+cellId+" isn't in cache and fs. It has to be created.");				
			F cellDao=createCellDAOBackground(cellId);
			log.info("The cell dao "+cellId+" has been created.");
			//C cell=createNewCellFromDAO(cellDao);
			return true;
		}else{
			return false;
		}
	}
	
	@OnBackground
	private F createCellDAOBackground(Vector2 cellId){
		return onNewCellDAO(cellId);
	}
}
