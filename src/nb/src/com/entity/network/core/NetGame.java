package com.entity.network.core;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.network.Network;
import com.entity.core.EntityManager;
import com.entity.network.core.dao.NetPlayerDAO;
import com.entity.network.core.dao.NetWorldDAO;
import com.entity.network.core.service.NetWorldService;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Server;
import com.jme3.network.service.serializer.ServerSerializerRegistrationsService;

public class NetGame{
	private Client netClient;
	private Server netServer;
	private String ip;	
	private String matchName;		
	private int port;
	private Network anot;
	
	private NetWorldService service;
	
	public NetGame(Network anot)throws Exception{
		this.anot=anot;
		port=anot.port();
                service=(NetWorldService) anot.worldService().newInstance();
	}
	
	public void addConnectionListener(ClientStateListener listener){
		if(isNetClientGame()){
			netClient.addClientStateListener(listener);
		}else if(isNetServerGame()){
			throw new RuntimeException("Trying to add clientstatelistener to a server!! You must add it to a client");
		}else{
			throw new RuntimeException("No network started");
		}
	}
	
	public void addConnectionListener(ConnectionListener listener){
		if(isNetServerGame()){
			netServer.addConnectionListener(listener);
		}else if(isNetClientGame()){
			throw new RuntimeException("Trying to add ConnectionListener to a client!! You must add it to a server");
		}else{
			throw new RuntimeException("No network started");
		}
	}
	
	public void removeConnectionListener(ClientStateListener listener){
		if(isNetClientGame()){
			netClient.removeClientStateListener(listener);
		}else if(isNetServerGame()){
			throw new RuntimeException("Trying to remove clientstatelistener to a server!! You must add it to a client");
		}else{
			throw new RuntimeException("No network started");
		}
	}
	
	public void removeConnectionListener(ConnectionListener listener){
		if(isNetServerGame()){
			netServer.removeConnectionListener(listener);
		}else if(isNetClientGame()){
			throw new RuntimeException("Trying to remove ConnectionListener to a client!! You must add it to a server");
		}else{
			throw new RuntimeException("No network started");
		}
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
	
	public NetWorldService getWorldService(){
		return service;
	}

	public void setWorldService(NetWorldService service) {
		this.service = service;
	}
}
