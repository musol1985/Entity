package com.entity.network.core.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

@com.jme3.network.serializing.Serializable
public abstract class NetWorldDAO<T extends NetPlayerDAO> implements Serializable{
	protected static final Logger log = Logger.getLogger(NetWorldDAO.class.getName());
	
	private HashMap<String, T> players=new HashMap<String, T>();
	private String id;
	private long timestamp;
	private int maxPlayers=10;
	private int maxRealSize=0;
	private boolean created;
	private String playerCreator;
	private GAME_MODE mode;
	private long seed;

	
	public enum GAME_MODE{NO_MODE, DEATHMATCH, COOPERATIVE}
	
	public NetWorldDAO(HashMap<String, T> players, String id, long timestamp, int maxRealSize) {
		this.players = players;
		this.id = id;
		this.timestamp=timestamp;
		this.maxRealSize=maxRealSize;
		this.mode=GAME_MODE.NO_MODE;
	}
	
	public NetWorldDAO() {

	}
	


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
	
	public T getNetPlayerById(String id){
		return players.get(id);
	}
	
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	public boolean isCreated() {
		return created;
	}
	public void setCreated(boolean created) {
		this.created = created;
	}
	
	
	public String getPlayerCreator() {
		return playerCreator;
	}
	public void setPlayerCreator(String playerCreator) {
		this.playerCreator = playerCreator;
	}

	public String getCachePath(){
		return "cache/"+id+"/";
	}
	/**
	 * Sets the max real size of the world or 0 to infinite world
	 * if max >0, the origin is 0,0 and grow to max,max
	 * if infinite, the size grows positive and negative
	 * @return
	 */
	public int getMaxRealSize() {
		return maxRealSize;
	}
	/**
	 * Return the max real size of the world or 0 to infinite world
	 * if max >0, the origin is 0,0 and grow to max,max
	 * if infinite, the size grows positive and negative
	 * @return
	 */
	public void setMaxRealSize(int maxRealSize) {
		this.maxRealSize = maxRealSize;
	}

	public GAME_MODE getMode() {
		return mode;
	}

	public void setMode(GAME_MODE mode) {
		this.mode = mode;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}
	
	
}