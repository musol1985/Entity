package com.entity.network.core.beans;

import java.util.ArrayList;
import java.util.List;

import com.entity.network.core.dao.NetWorldCellDAO;
import com.entity.utils.Vector2;
import com.jme3.network.HostedConnection;

public class CreatingCell {
	private Vector2 cellId;
	private NetWorldCellDAO cellDao;
	private List<HostedConnection> players=new ArrayList<HostedConnection>();
	
	
	public CreatingCell(Vector2 cellId, HostedConnection player) {
		this.cellId = cellId;
		players.add(player);
	}


	public Vector2 getCellId() {
		return cellId;
	}


	public void setCellId(Vector2 cellId) {
		this.cellId = cellId;
	}


	public NetWorldCellDAO getCellDao() {
		return cellDao;
	}


	public void setCellDao(NetWorldCellDAO cellDao) {
		this.cellDao = cellDao;
	}


	public List<HostedConnection> getPlayers() {
		return players;
	}


	public void setPlayers(List<HostedConnection> players) {
		this.players = players;
	}
	
	public void addPlayer(HostedConnection cnn){
		if(!players.contains(cnn)){
			players.add(cnn);
		}
	}

}
