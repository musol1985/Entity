package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.network.core.items.InGameClientScene;
import com.entity.network.core.msg.MsgShowCells;
import com.entity.utils.Vector2;
import com.jme3.network.MessageConnection;
import java.util.concurrent.Callable;

public class InGameClientMessageListener extends NetworkMessageListener<InGameClientScene>{
	
	public void onShowCells(final MsgShowCells msg, MessageConnection cnn)throws Exception{
		getEntity().getApp().enqueue(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        for(NetWorldCellDAO cell:msg.cellsToReload){
                                getEntity().getService().reloadCell(cell);
                        }
                        for(Vector2 cId:msg.cellsToReuse){
                                getEntity().getService().reuseCell(cId);
                        }
                        return true;
                    }
		});

	}
}
