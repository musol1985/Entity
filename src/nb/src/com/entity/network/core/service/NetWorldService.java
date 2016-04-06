package com.entity.network.core.service;

import java.util.HashMap;
import java.util.logging.Logger;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.Task;
import com.entity.core.EntityManager;
import com.entity.core.items.BaseService;
import com.entity.core.items.interceptors.ThreadsMethodInterceptor;
import com.entity.network.core.beans.CellId;
import com.entity.network.core.beans.CellViewQuad;
import com.entity.network.core.builders.NetWorldServiceBuilder;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.items.IWorldInGameScene;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.models.NetWorldCell;
import com.entity.network.core.tasks.NetWorldPersistTask;
import com.entity.utils.Vector2;
import com.jme3.math.Vector3f;

//@BuilderDefinition(builderClass=NetWorldServiceBuilder.class, methodInterceptorClass=ThreadsMethodInterceptor.class)
public abstract class NetWorldService<W extends NetWorld, P extends NetPlayer, C extends NetWorldCell, D extends NetWorldDAO<E>, E extends NetPlayerDAO, F extends NetWorldCellDAO> extends BaseService{
	protected static final Logger log = Logger.getLogger(NetWorldService.class.getName());

	protected W world;
	protected P player;


	
	/**
	 * Get a cell by ID
	 * 
	 * @param id
	 * @return cell
	 */
	public abstract C getCellById(Vector2 cellId);
	

	
	/**
	 * Create a PlayerDAO
	 * @param name
	 * @return
	 */
	public abstract void onNewPlayerDAO(E player);

	
	/**
	 * Returns the class that implements the NetworldCell
	 * @return
	 */
	public abstract Class<C> getCellClass();
	
	/**
	 * Returns the class that implements the NetWorld
	 * @return
	 */
	public abstract Class<W> getWorldClass();
	
	/**
	 * Returns the class that implements the NetPlayer
	 * @return
	 */
	public abstract Class<P> getPlayerClass();


	/**
	 * Returns the class that implements the NetPlayerDAO
	 * @return
	 */
	public abstract Class<E> getPlayerDAOClass();
	
	
	public C getCellFromFS(Vector2 cellId){
		NetWorldCellDAO cell=(NetWorldCellDAO)EntityManager.loadPersistable(world.getDao().getCachePath()+cellId+".cache");
		if(cell!=null){
			Class c=getCellClass();
			if(c==null){
				throw new RuntimeException("You must implement getCellClass on NetWorldService.getCellFromFS");
			}
			return (C)EntityManager.instanceGeneric(c, cell);
		}
		return null;
	}
	
	protected C getCellFromCache(Vector2 cellId){
		return (C) world.getCells().get(cellId);
	}
	
	protected NetWorldCellDAO getCellIdFromIndexes(Vector2 cellId){
		return (NetWorldCellDAO) world.cellsIndex.get(cellId);
	}
	
	public void saveCellFS(F cell){
		EntityManager.savePersistable(world.getDao().getCachePath()+cell.getId().toFileName()+".cache", cell);		
	}
	

	
	public boolean isCellInLimits(Vector2 v){
		if(world.getDao().getMaxRealSize()==NetWorldDAO.INFINITE_SIZE)return true;
		
		return v.x>=0 && v.x<world.getDao().getMaxRealSize() && v.z>=0 && v.z<world.getDao().getMaxRealSize();
	}
	
	
	
	/**
	 * If overrides, it must call super
	 * @param cell
	 */
	public void onCellPopCache(C cell){
		//saveCellFS(cell);
	}

	
	

	public D getWorldDAO() {
            if(world!=null)
		return (D)world.getDao();
            return null;
	}


	public void setWorldDAO(D world) {
		try{
			if(this.world==null){			
				log.warning("WorldService: world model is null, create temp world model to set the worldDao");		
				this.world=(W)getWorldClass().newInstance();
                                this.world.setTemporal(true);
			}
			this.world.setDao(world);
		}catch(Exception e){
			e.printStackTrace();
			log.severe("Error setting worldDAO");
		}
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
	 * Create a new PlayerDAO from nickname
	 * @param name
	 * @return
	 */
	public E createNewPlayerDAO(String name){
		try{
			E player=getPlayerDAOClass().newInstance();
			player.setId(name);
			onNewPlayerDAO(player);
			return player;
		}catch(Exception e){
			log.severe("Cant create a new player dao");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Create a new CellModel from a CellDAO and put it in indexes and cache
	 * @param dao
	 * @return
	 */
	public C createNewCellFromDAO(F dao){
		C cell=(C)EntityManager.instanceGeneric(getCellClass(), dao);
		log.info("The cell "+dao.getId()+" has been created. Inserting in cache and in indexes. TS: "+dao.getId().timestamp);		
		world.cellsIndex.put(dao.getId().id, cell.dao.getId());
		world.getCells().put(dao.getId().id, cell);
		return cell;
	}
	
	public abstract int getCellCacheSize();
        
        
        public void initWorld(){
		HashMap<String, P> players=new HashMap<String, P>(world.dao.getPlayers().size());
		
		for(Object p:world.dao.getPlayers().values()){
			E pDAO=(E)p;
			if(getPlayer()==null || !pDAO.getId().equals(getPlayer().dao.getId())){
				P player=(P)EntityManager.instanceGeneric(getPlayerClass(), pDAO);				
				players.put(pDAO.getId(), player);
			}
		}
		
		world.setPlayers(players);
	}

        
    public void onUpdateCell(C cell){
    	((IWorldInGameScene)EntityManager.getCurrentScene()).getPersistTask().persistCell(cell.dao);
    }
}
