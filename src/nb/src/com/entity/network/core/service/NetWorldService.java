package com.entity.network.core.service;

import java.util.logging.Logger;

import com.entity.core.EntityManager;
import com.entity.network.core.beans.ViewerCell;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.utils.Vector2;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;

public abstract class NetWorldService<W extends NetWorld, P extends NetPlayer, C extends NetWorldCellDAO, D extends NetWorldDAO<E>, E extends NetPlayerDAO> {
	protected static final Logger log = Logger.getLogger(NetWorldService.class.getName());

	protected W world;
	protected P player;
	
	
	
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
		return (C) world.getCells().get(cellId);
	}
	
	private void saveCellFS(C cell){
		EntityManager.savePersistable(world.getDao().getCachePath()+cell.getId()+".cache", cell);		
	}
	
	public abstract C createNewCell(Vector2 cellId);
	public abstract E createNewPlayerDAO(String name);
	
	public boolean isCellInLimits(Vector2 v){
		if(world.getDao().getMaxRealSize()==NetWorldDAO.INFINITE_SIZE)return true;
		
		return v.x>=0 && v.x<world.getDao().getMaxRealSize() && v.z>=0 && v.z<world.getDao().getMaxRealSize();
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
		for(Object player:world.getDao().getPlayers().values()){
			if(((E)player).getCnn()==cnn)
				return (E)player;
		}
		return null;
	}

	public D getWorldDAO() {
		return (D)world.getDao();
	}

	public void setWorldDAO(D world) {
		if(world==null){			
			log.warning("WorldService: world model is null, create temp world model to set the worldDao");		
			this.world=(W) new NetWorld(){
				@Override
				public int getCellSize() {
					return 0;
				}

				@Override
				public boolean isTemporal() {
					return true;
				}				
			};
				
		}
		this.world.setDao(world);
	}


	public E getPlayerDAO() {
		return (E)player.getDao();
	}


	public void setPlayerDAO(E playerDAO) {
		if(player==null){
			log.warning("WorldService: player model is null, create temp player model to set the plauyerDao");		
			this.player=(P) new NetPlayer(){};
		}
		this.player.setDao(playerDAO);
	}


	public W getWorld() {
		return world;
	}


	public void setWorld(W world) {
		this.world = world;
	}


	public P getPlayer() {
		return player;
	}


	public void setPlayer(P player) {
		this.player = player;
	}
	
	public boolean isWorldSelected(){
		return world!=null;
	}

	public Vector2 getCellPosByReal(Vector3f pos){
		Vector2 res= new Vector2((int)pos.x/world.getCellSize(),(int)pos.z/world.getCellSize());
		if(isCellInLimits(res))
			return res;
		
		return null;
	}
	
	public Vector2 getVirtualPosByReal(Vector3f pos){
		return new Vector2((int)Math.floor((pos.x-world.getVirtualCellSize())/world.getCellSize()),(int)Math.floor((pos.z-world.getVirtualCellSize())/world.getCellSize()));
	}
	
	public ViewerCell getByVirtualCell(Vector2 virtual){
		Vector3f real=getRealFromVirtual(virtual);
		ViewerCell v=new ViewerCell();
		v.cell0=getCellPosByReal(real.add(0, 0, 0));
		v.cell1=getCellPosByReal(real.add(world.getCellSize(), 0, 0));
		v.cell2=getCellPosByReal(real.add(world.getCellSize(), 0, world.getCellSize()));
		v.cell3=getCellPosByReal(real.add(0, 0, world.getCellSize()));
		
		return v;
	}
	
	public Vector3f getRealFromVirtual(Vector2 virtual){
		return new Vector3f(virtual.x*world.getCellSize(),0,virtual.z*world.getCellSize());
	}

}
