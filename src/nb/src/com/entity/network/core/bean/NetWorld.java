package com.entity.network.core.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

@com.jme3.network.serializing.Serializable
public class NetWorld<T extends NetPlayer> implements Serializable{
	private HashMap<String, T> players;
	private String id;
	private long timestamp;
	private int maxPlayers=10;
	private transient boolean created;
	private String playerCreator;
	
	public HashMap<String, T> getPlayers() {
		return players;
	}
	public void setPlayers(HashMap<String, T> players) {
		this.players = players;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public NetWorld(HashMap<String, T> players, String id, long timestamp) {
		this.players = players;
		this.id = id;
		this.timestamp=timestamp;
	}
	public NetWorld() {

	}
	
	public T getNetPlayerById(String id){
		return players.get(id);
	}
	
	public boolean isAllPlayersReady(){
		for(Entry<String,T> p:players.entrySet()){
			if(!p.getValue().isReady())
				return false;
		}
			
		return true;
	}
	public int getMaxPlayers() {
		return maxPlayers;
	}
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	public boolean isNewCreated() {
		return created;
	}
	public void setCreated(boolean created) {
		this.created = created;
	}
	
	public NetPlayer getNewPlayer(){
		return new NetPlayer();
	}
	public String getPlayerCreator() {
		return playerCreator;
	}
	public void setPlayerCreator(String playerCreator) {
		this.playerCreator = playerCreator;
	}
	
}
