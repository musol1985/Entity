package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.items.InGameClientScene;
import com.entity.network.core.msg.MsgShowCells;
import com.entity.utils.Vector2;
import com.jme3.network.MessageConnection;

public class InGameClientMessageListener extends NetworkMessageListener<InGameClientScene>{
	
	public void onShowCells(MsgShowCells msg, MessageConnection cnn)throws Exception{
		for(NetWorldCellDAO cell:msg.cellsToReload){
			getEntity().getService().reloadCell(cell);
		}
		for(Vector2 cId:msg.cellsToReuse){
			getEntity().getService().reuseCell(cId);
		}
	}
}
