package com.entity.network.core.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.entity.anot.OnExecutor;
import com.entity.anot.RunOnGLThread;
import com.entity.core.EntityManager;
import com.entity.network.core.beans.CreatingCell;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.models.NetWorldCell;
import com.entity.network.core.msg.MsgShowCell;
import com.entity.network.core.service.NetWorldService;
import com.entity.utils.Vector2;
import com.jme3.network.HostedConnection;

public abstract class ServerNetWorldService<W extends NetWorld, P extends NetPlayer, C extends NetWorldCell, D extends NetWorldDAO<E>, E extends NetPlayerDAO, F extends NetWorldCellDAO> extends NetWorldService<W,P,C,D,E,F>{
	
	protected ConcurrentHashMap<Vector2, CreatingCell> creatingCell = new ConcurrentHashMap<Vector2, CreatingCell>();
	
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
	 * Get a cell by ID 
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
			}else{				
				log.info("getcellById "+cellId+" doesn't exist. It must be created");
			}
						
		}
		return cell;
	}
	
	/**
	 * Create a new cell on background
	 * when the cell is created, it will send to the players that request it
	 * @param cellId
	 * @param cnn
	 */
	public void createNewCell(Vector2 cellId, HostedConnection cnn){
		if(isCellInLimits(cellId)){
			CreatingCell creating=creatingCell.get(cellId);
			if(creating!=null){
				creating.addPlayer(cnn);
			}else{
				creatingCell.put(cellId, new CreatingCell(cellId, cnn));
				createCellDAOBackground(creating);
			}		
		}
	}
	
	
	@OnExecutor
	private void createCellDAOBackground(CreatingCell creating){
		F dao=onNewCellDAO(creating.getCellId());
		creating.setCellDao(dao);
		
		createCellModelFromDAOBackground(creating);
	}
	
	/**
	 * Create a new CellModel from a CellDAO and put it in indexes and cache
	 * then it removes from creatingcell and send to all the players requested the cell
	 */
	@RunOnGLThread
	private void createCellModelFromDAOBackground(CreatingCell creating){
		for(HostedConnection cnn:creating.getPlayers()){
			cnn.send(new MsgShowCell(creating.getCellDao()));
		}
		
		C cell=createNewCellFromDAO((F)creating.getCellDao());
		
		creatingCell.remove(creating.getCellId());
	}

}
