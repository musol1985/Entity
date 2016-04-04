package com.entity.network.core.tasks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.entity.adapters.TaskAdapter;
import com.entity.core.EntityManager;
import com.entity.core.items.Scene;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.items.IWorldInGameScene;
import com.entity.network.core.models.NetWorld;
import com.entity.network.core.service.NetWorldService;
import com.entity.utils.Vector2;

public class NetWorldPersistTask extends TaskAdapter<Scene, NetWorldService>{
	protected static final Logger log = Logger.getLogger(NetWorldPersistTask.class.getName());
	
	private NetWorldService service;
	private ConcurrentHashMap<Vector2, NetWorldCellDAO> cells=new ConcurrentHashMap<Vector2, NetWorldCellDAO>();
	private int index;
	
	@Override
	public void onCreate(NetWorldService entity) throws Exception {
		this.service=entity;
	}
	
        public void setIndex(int index){
            this.index=index;
        }

	@Override
	public void run() {
            if(service.getWorld()!=null && !service.getWorld().isTemporal()){
		log.info("Starting save to FS");
		if(cells.size()>0){
			for(NetWorldCellDAO cell:cells.values()){
				log.info("Saving to FS "+cell.getId());
				service.saveCellFS(cell);
			}
			cells.clear();
		}
		if(index!=service.getWorld().getCellsIndex().size()){
			log.info("Saving indexes: "+index+"->"+service.getWorld().getCellsIndex().size());
			index=getWorld().saveWolrdIndexesFS();			
		}
		log.info("Finish save to FS");
            }
	}

	public NetWorldService getService(){
		return ((IWorldInGameScene)getScene()).getService();
	}
	
	public NetWorld getWorld(){
		return ((IWorldInGameScene)getScene()).getWorld();
	}

	public void persistCell(NetWorldCellDAO cell){
		if(!cells.containsKey(cell.getId().id))
			cells.put(cell.getId().id, cell);
	}
}
