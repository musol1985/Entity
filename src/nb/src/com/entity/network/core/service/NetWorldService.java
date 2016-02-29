package com.entity.network.core.service;

import java.util.List;
import java.util.logging.Logger;

import com.entity.core.EntityManager;
import com.entity.network.core.beans.CellId;
import com.entity.network.core.beans.CellViewQuad;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.models.NetWorldCell;
import com.entity.network.core.msg.MsgGetCells;
import com.entity.utils.Vector2;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;

public abstract class NetWorldService<W extends NetWorld, P extends NetPlayer, C extends NetWorldCell, D extends NetWorldDAO<E>, E extends NetPlayerDAO> {
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
				
				//Only if is server
				if(canCreateCell() && cell==null){
					log.info("The cell "+cellId+" isn't in cache and fs. It has to be created.");
					cell=createNewCell(cellId);
				}
			}
		}
		
		return cell;
		
	}
	
	private C getCellFromFS(Vector2 cellId){
		NetWorldCellDAO cell=(NetWorldCellDAO)EntityManager.loadPersistable(world.getDao().getCachePath()+cellId+".cache");
		if(cell!=null){
			Class c=getCellClass();
			EntityManager.instanceGeneric(c, cell);
		}
	}
	
	private C getCellFromCache(Vector2 cellId){
		return (C) world.getCells().get(cellId);
	}
	
	private void saveCellFS(C cell){
		EntityManager.savePersistable(world.getDao().getCachePath()+cell.getDao().getId()+".cache", cell.dao);		
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
	 * Preload the world when starting the game the first time
	 * Example: position player, initial values, seeds, etc.
	 */
	public abstract W createTempNetWorld();
	
	/**
	 * Returns the class of the NetworldCell
	 * @return
	 */
	public abstract Class getCellClass();
	
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
            if(world!=null)
		return (D)world.getDao();
            return null;
	}

	public void setWorldDAO(D world) {
		if(this.world==null){			
			log.warning("WorldService: world model is null, create temp world model to set the worldDao");		
			this.world=createTempNetWorld();
                        this.world.setTemporal(true);
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

	/**
	 * Gets the cell position from a real position
	 * @param pos
	 * @return
	 */
	public Vector2 getCellPosByReal(Vector3f pos){
		Vector2 res= new Vector2((int)pos.x/world.getCellSize(),(int)pos.z/world.getCellSize());
		if(isCellInLimits(res))
			return res;
		
		return null;
	}
	
	/**
	 * returns a virtual position from a real position
	 * @param pos
	 * @return
	 */
	public Vector2 getVirtualPosByReal(Vector3f pos){
		return new Vector2((int)Math.floor((pos.x-world.getVirtualCellSize())/world.getCellSize()),(int)Math.floor((pos.z-world.getVirtualCellSize())/world.getCellSize()));
	}
	
	/**
	 * Returns view from a real position
	 * @param pos
	 * @return
	 */
	public CellViewQuad getViewByRealPosition(Vector3f pos){
		return getViewByVirtualCell(getVirtualPosByReal(pos));
	}
	
	/**
	 * Returns the view quad from a virtual cell position
	 * @param virtual
	 * @return
	 */
	public CellViewQuad getViewByVirtualCell(Vector2 virtual){
		Vector3f real=getRealFromVirtual(virtual);
		CellViewQuad v=new CellViewQuad();
		v.setCell0(getCellId(getCellPosByReal(real.add(0, 0, 0))));
		v.setCell1(getCellId(getCellPosByReal(real.add(world.getCellSize(), 0, 0))));
		v.setCell2(getCellId(getCellPosByReal(real.add(world.getCellSize(), 0, world.getCellSize()))));
		v.setCell3(getCellId(getCellPosByReal(real.add(0, 0, world.getCellSize()))));
		
		return v;
	}
	
	/**
	 * Gets the celldId(pos, timestamp) from cell position
	 * if it doesn't exist in world.cellIndex, it will create and put timestamp -1 and put in cellIndex
	 * @param pos
	 * @return
	 */
	private CellId getCellId(Vector2 pos){
		if(pos==null)
			return null;
		
		CellId id=(CellId)world.getCellsIndex().get(pos);
		if(id==null){
			id=new CellId(pos, -1);
			world.getCellsIndex().put(pos,  id);
		}
		return id;
	}
	
	public Vector3f getRealFromVirtual(Vector2 virtual){
		return new Vector3f(virtual.x*world.getCellSize(),0,virtual.z*world.getCellSize());
	}

	public void setPayerReady(String playerId){
		((NetPlayerDAO)world.getDao().getPlayers().get(playerId)).setReady(true);
	}
	
	public boolean allPlayersReady(){
		for(Object obj:world.getDao().getPlayers().values())
			if(!((NetPlayerDAO)obj).isReady())
				return false;
		return true;
	}
	
	/**
	 * Checks if the player visor has change and then try to load the Cells
	 * @param position
	 */
	public void updatePlayerLocation(Vector3f position){
		CellViewQuad newView=getViewByRealPosition(position);
		
		if(!world.getView().equals(newView)){
			
			log.info("On new CellViewQuad-> "+newView);
			CellViewQuad oldView=world.getView();				
			world.setView(newView);
			
			List<CellId> cellsToLoad=newView.getCellsNotIn(oldView);	

			MsgGetCells msg=new MsgGetCells(cellsToLoad);
			msg.send();
			
			List<CellId> cellsToUnLoad=oldView.getCellsNotIn(newView);
			for(CellId c:cellsToUnLoad){
				log.info("Unloading cell..."+c);
			}
		}
	}
	
	
	

}
