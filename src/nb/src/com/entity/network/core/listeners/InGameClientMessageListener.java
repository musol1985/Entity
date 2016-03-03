package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.network.core.items.InGameClientScene;
import com.entity.network.core.msg.MsgShowCell;
import com.entity.network.core.msg.MsgShowCells;
import com.jme3.network.MessageConnection;

public class InGameClientMessageListener extends NetworkMessageListener<InGameClientScene>{
	
	public void onShowCells(MsgShowCells msg, MessageConnection cnn)throws Exception{
		getEntity().getService().showCells(msg.cellsToReload, msg.cellsToReuse);
	}
	
	public void onShowCell(MsgShowCell msg)throws Exception{
		getEntity().getService().showCell(msg.cell);
	}
}
