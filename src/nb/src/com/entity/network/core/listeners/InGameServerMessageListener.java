package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.network.core.beans.CellId;
import com.entity.network.core.items.InGameServerScene;
import com.entity.network.core.models.NetWorldCell;
import com.entity.network.core.msg.MsgGetCells;
import com.entity.network.core.msg.MsgShowCells;
import com.jme3.network.HostedConnection;

public class InGameServerMessageListener extends NetworkMessageListener<InGameServerScene>{
	public void onGetCells(MsgGetCells msg, HostedConnection cnn)throws Exception{
		MsgShowCells res=MsgShowCells.getNew();
		
		for(CellId c:msg.cells){
			log.info("onGetCells: "+c.getId());
			NetWorldCell cell=getEntity().getService().getCellById(c.id);
			
			if(cell!=null){
				if(c.timestamp<cell.getDao().getId().timestamp){
					log.info("onGetCells: "+c.getId()+" newer on server, must be sent to client");				
					res.cellsToReload.add(cell.getDao());
				}else if(c.timestamp==cell.getDao().getId().timestamp){
					log.info("onGetCells: "+c.getId()+" same timestamp, client must load his cell");
					res.cellsToReuse.add(c.id);				
				}else{
					log.severe("onGetCells: "+c.getId()+" client cell timestamp is newer than the server timestamp");
				}		
			}else{
				log.info("onGetCells: "+c.getId()+" doesn't exist, we create it");
				getEntity().getService().createNewCell(c.getId(), cnn);
				log.info("onGetCells: "+c.getId()+" is creating in background, it will be sent to the client automatically");
			}
		}
		
		if(res.hasCells())
			res.send();
	}
 
}
