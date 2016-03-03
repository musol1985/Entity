package com.entity.network.core.service.impl;

import java.util.List;

import com.entity.anot.RunOnGLThread;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.entity.network.core.beans.CellId;
import com.entity.network.core.beans.CellViewQuad;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.models.NetPlayer;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.models.NetWorldCell;
import com.entity.network.core.msg.MsgGetCells;
import com.entity.network.core.service.NetWorldService;
import com.entity.utils.Vector2;
import com.jme3.math.Vector3f;

public abstract class ClientNetWorldService<W extends NetWorld, P extends NetPlayer, C extends NetWorldCell, D extends NetWorldDAO<E>, E extends NetPlayerDAO, F extends NetWorldCellDAO> extends NetWorldService<W,P,C,D,E,F>{
	/**
	 * Get a cell by ID from cache or fs(cellId must be in indexes)
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
					log.severe("Cell "+cellId+" isn't in indexes. ");
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
	 * Checks if the player visor has change and then try to load the Cells
	 * @param position
	 */
	public void updatePlayerLocation(Vector3f position){
		CellViewQuad newView=getViewByRealPosition(position);
		
		if(world.getView()==null || !world.getView().equals(newView)){
			
			log.info("On new CellViewQuad-> "+newView);
			CellViewQuad oldView=world.getView();				
			world.setView(newView);
			
			List<CellId> cellsToLoad=newView.getCellsNotIn(oldView);	

			MsgGetCells msg=new MsgGetCells(cellsToLoad);
			msg.send();
			
			if(oldView!=null){
				List<CellId> cellsToUnLoad=oldView.getCellsNotIn(newView);
				for(CellId c:cellsToUnLoad){
					log.info("Unloading cell..."+c);
					//TODO unload cells
				}
			}
		}
	}
	
	/**
	 * Reloads(newer CellDAO) or reuses(cached CellDAO on cache or FS) a cellDAO
	 * it is running on @RunOnGLThread
	 * @param reloadCells
	 * @param reuseCells
	 */
	@RunOnGLThread
	public void showCells(List<F> reloadCells, List<Vector2> reuseCells){
		 for(F cell:reloadCells){
             reloadCell(cell);
	     }
	     for(Vector2 cId:reuseCells){
	         reuseCell(cId);
	     }
	}
	
	
	/**
	 * Reloads(newer CellDAO) or reuses(cached CellDAO on cache or FS) a cellDAO
	 * it is running on @RunOnGLThread
	 * @param reloadCells
	 * @param reuseCells
	 */
	@RunOnGLThread
	public void showCell(F cell){
		reloadCell(cell);
	}
	
	/**
	 * Get a cell from cache or FS, load it in cache and index and 
	 * @param cells
	 */
	public void reuseCell(Vector2 id){
		log.info("Reusing cell "+id);
		C cell=getCellById(id);
		
		//TODO attach cell
	}
	
	public void reloadCell(F cellDao){
		log.info("Reloading cell "+cellDao.getId().id);
		C cell=createNewCellFromDAO(cellDao);
		try {
			log.info("Position of cell "+cellDao.getId()+" ->"+getRealFromVirtual(cellDao.getId().id));
			cell.setLocalTranslation(getRealFromVirtual(cellDao.getId().id));
			if(world.getView()!=null && world.getView().hasCell(cell.dao.getId())){
				cell.attachToParent(world);
			}else{
				log.info("The cell "+cellDao.getId().id+" isn't in the view. We don't attach it to the world");
			}
		} catch (Exception e) {
			log.severe("Error attaching to parent the cell "+cellDao.getId());
			e.printStackTrace();			
		}
	}
}
