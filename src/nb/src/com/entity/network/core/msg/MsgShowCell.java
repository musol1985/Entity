package com.entity.network.core.msg;

import com.entity.network.core.dao.NetWorldCellDAO;
import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgShowCell extends BaseNetMessage {
	public NetWorldCellDAO cell;


	
	public MsgShowCell(NetWorldCellDAO cell) {
		this.cell = cell;
	}



	public MsgShowCell() {
		
	}
	


}
