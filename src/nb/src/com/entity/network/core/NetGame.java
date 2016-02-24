package com.entity.network.core;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.network.Network;
import com.entity.core.EntityManager;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.jme3.network.Client;
import com.jme3.network.Server;
import com.jme3.network.service.serializer.ServerSerializerRegistrationsService;

public class NetGame{
	private Client netClient;
	private Server netServer;
	private String ip;	
	private String matchName;		
	private int port;
	private Network anot;
	private NetWorld world;
	private NetPlayer player;
	
	public NetGame(Network anot){
		this.anot=anot;
		port=anot.port();
	}
	
	public void addMsgListener(NetworkMessageListener listener){
		if(isNetClientGame()){
			netClient.addMessageListener(listener);
		}else if(isNetServerGame()){
			netServer.addMessageListener(listener);
		}else{
			throw new RuntimeException("No network started");
		}
	}
	
	public void removeMsgListener(NetworkMessageListener listener){
		if(isNetClientGame()){
			netClient.removeMessageListener(listener);
		}else if(isNetServerGame()){
			netServer.removeMessageListener(listener);
		}else{
			throw new RuntimeException("No network started");
		}
	}
	
	public Server getServer(){
		return netServer;
	}
	
	public Client getClient(){
		return netClient;
	}
	
	public void setNetwork(Object net){
		if(isNetworkGame()){
			if(net instanceof Server){
				netServer=(Server)net;
				netServer.getServices().removeService(netServer.getServices().getService(ServerSerializerRegistrationsService.class));
			}else{
				netClient=(Client)net;
			}
			return;
		}
		throw new RuntimeException("No network annotation present");
	}
	
	public boolean isNetworkGame(){
		return anot!=null || isNetClientGame() || isNetServerGame();
	}	
	
	public boolean isNetClientGame(){
		return netClient!=null;
	}	
	
	public boolean isNetServerGame(){
		return netServer!=null;
	}	
	
	public Network getNetworkOptions(){
		if(isNetworkGame()){
			return anot;
		}
		throw new RuntimeException("No network annotation present");
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public NetWorld getWorld() {
		return world;
	}

	public void setWorld(NetWorld world) {
		this.world = world;
	}

	public NetPlayer getPlayer() {
		return player;
	}

	public void setPlayer(NetPlayer player) {
		this.player = player;
	}
	
	public boolean isWorldSelected(){
		return EntityManager.getGame().getNet().getWorld()!=null;
	}
}
