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
	public abstract void preload() throws Exception;
	

	
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
				world.getCells().remove(cellId);
				world.getCells().put(cellId, cell);
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
	 * returns the cellmodel if background=false, else it returns null
	 */
	public C createNewCell(Vector2 cellId, HostedConnection cnn, boolean background)throws Exception{
		if(isCellInLimits(cellId)){
			CreatingCell creating=creatingCell.get(cellId);
			if(creating!=null){
                                log.info("getcellById "+cellId+" is creating, we put the player in the listener list");
				creating.addPlayer(cnn);
			}else{
                                creating=new CreatingCell(cellId, cnn);
				creatingCell.put(cellId, creating);
                                log.info("getcellById "+cellId+" we start to creating the cell in background");
                if(background){                
                	createCellDAOBackground(creating);
                }else{
                	createCellDAO(creating);
                	return createCellModelFromDAO(creating);
                }
			}		
		}
		return null;
	}
	
	
	@OnExecutor
	public void createCellDAOBackground(CreatingCell creating)throws Exception{
		log.info("getcellById "+creating.getCellId()+" begin the creation of cell dao in background thread");
		createCellDAO(creating);
		log.info("getcellById "+creating.getCellId()+" has been created in background. Now create de cellModel");
		createCellModelFromDAOBackground(creating);
	}
	
	public void createCellDAO(CreatingCell creating)throws Exception{                
		F dao=onNewCellDAO(creating.getCellId());
		creating.setCellDao(dao);				
	}
	
	/**
	 * Create a new CellModel from a CellDAO and put it in indexes and cache
	 * then it removes from creatingcell and send to all the players requested the cell
	 */
	@RunOnGLThread
	public void createCellModelFromDAOBackground(CreatingCell creating)throws Exception{
		createCellModelFromDAO(creating);
	}
	

	public C createCellModelFromDAO(CreatingCell creating)throws Exception{
		for(HostedConnection cnn:creating.getPlayers()){
                    if(cnn!=null){
                        log.info("getcellById sending the cell "+creating.getCellId()+" to the connection "+cnn.getAddress());
			cnn.send(new MsgShowCell(creating.getCellDao()));
                    }
		}
		log.info("getcellById "+creating.getCellId()+" creating the cellModel in GLThread");
		C cell=createNewCellFromDAO((F)creating.getCellDao());
		log.info("getcellById "+creating.getCellId()+" created the cellModel in GLThread");
		creatingCell.remove(creating.getCellId());
		
		log.info("Position of cell "+cell.getDao().getId()+" ->"+getRealFromVirtual(cell.getDao().getId().id));
		cell.setLocalTranslation(getRealFromVirtual(cell.getDao().getId().id));          
		System.out.println("*************************************************"+Thread.currentThread());
		cell.attachToParent(world);
		dettachUnusedCells();
		
		onUpdateCell(cell);
		
		return cell;
	}
	
	
	private void dettachUnusedCells(){
		//TODO dettach unused cells(no dynamic bodies in)
	}

	
}
