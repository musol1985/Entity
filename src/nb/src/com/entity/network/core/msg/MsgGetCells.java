package com.entity.network.core.msg;

import java.util.List;

import com.entity.network.core.beans.CellId;
import com.jme3.network.serializing.Serializable;

@Serializable
public class MsgGetCells extends BaseNetMessage {
	public List<CellId> cells;

	public MsgGetCells(List<CellId> cells) {
		this.cells=cells;
	}
	
	public MsgGetCells() {
		
	}
}
