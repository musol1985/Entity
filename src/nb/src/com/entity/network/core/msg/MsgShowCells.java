package com.entity.network.core.msg;

import java.util.ArrayList;
import java.util.List;

import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.utils.Vector2;
import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgShowCells extends BaseNetMessage {
	public List<NetWorldCellDAO> cellsToReload;
	public List<Vector2> cellsToReuse;

	public static MsgShowCells getNew() {
		MsgShowCells res=new MsgShowCells();
		res.cellsToReload=new ArrayList<NetWorldCellDAO>();
		res.cellsToReuse=new ArrayList<Vector2>();
		return res;
	}
	
	public MsgShowCells() {
		
	}
	
	public boolean hasCells(){
		return cellsToReload.size()>0 || cellsToReuse.size()>0;
	}

}
