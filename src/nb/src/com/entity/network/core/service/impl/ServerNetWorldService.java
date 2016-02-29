package com.entity.network.core.service.impl;

import com.entity.core.EntityManager;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.models.NetWorldCell;
import com.entity.network.core.service.NetWorldService;
import com.entity.utils.Vector2;
import com.jme3.network.HostedConnection;

public abstract class ServerNetWorldService<W extends NetWorld, P extends NetPlayer, C extends NetWorldCell, D extends NetWorldDAO<E>, E extends NetPlayerDAO> extends NetWorldService<W,P,C,D,E>{
	
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
	 * Get a cell by ID if not exist, it creates the cell, put in cache and in indexes
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
				}											
			}
			
			if(cell==null){
				log.info("The cell "+cellId+" isn't in cache and fs. It has to be created.");
				cell=createNewCell(cellId);
				log.info("The cell "+cellId+" has been created. Inserting in cache and in indexes");
				world.cellsIndex.put(cellId, cell.dao);
				world.cellsCache.put(cellId, cell);
			}else{
				log.info("The cell "+cellId+" already exist. Put on first place in cache.");
				world.cellsCache.remove(cellId);
				world.cellsCache.put(cellId, cell);
			}
		}
		return cell;
	}

	@Override
	public C createNewCell(Vector2 cellId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E createNewPlayerDAO(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class getCellClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public W createTempNetWorld() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
