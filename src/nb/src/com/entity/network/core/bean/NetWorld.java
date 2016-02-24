package com.entity.network.core.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.entity.network.core.service.WorldService;
import com.entity.utils.Vector2;

@com.jme3.network.serializing.Serializable
public abstract class NetWorld<T extends NetPlayer, S extends WorldService, C extends NetWorldCell> implements Serializable{
	protected static final Logger log = Logger.getLogger(NetWorld.class.getName());
	
	public static final int CELLS_SIZE = 10;
	private static float HASH_TABLE_LOAD_FACTOR=0.75f;
	private static int HASH_TABLE_CAPACITY = (int) Math.ceil(CELLS_SIZE / HASH_TABLE_LOAD_FACTOR) + 1;
	
	private HashMap<String, T> players=new HashMap<String, T>();
	private String id;
	private long timestamp;
	private int maxPlayers=10;
	private boolean created;
	private String playerCreator;
	
	
	private transient S service;
	private transient LinkedHashMap<Vector2, C> cells;
	
	public NetWorld(HashMap<String, T> players, String id, long timestamp) {
		this();
		this.players = players;
		this.id = id;
		this.timestamp=timestamp;
	}
	
	public NetWorld() {
		init();	
	}
	
	public void init(){
		cells=new LinkedHashMap<Vector2, C>(HASH_TABLE_CAPACITY, HASH_TABLE_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Vector2, C> eldest) {
                boolean pop=this.size() > CELLS_SIZE;
                if(pop){
                	service.onCellPopCache(eldest.getValue());                    
                }
                return pop;
            }
		};
		service=initWorldService();	
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
	
	public abstract S initWorldService();
	
	public S getService(){
		return service;
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
	public LinkedHashMap<Vector2, C> getCells() {
		return cells;
	}
	public void setCells(LinkedHashMap<Vector2, C> cells) {
		this.cells = cells;
	}
	public String getCachePath(){
		return "cache/"+id+"/";
	}
	
}
