package com.entity.test;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.network.Broadcast;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;

public class GameMessageListener extends NetworkMessageListener{

	public void play(){
		
	}
	
	public void play(Message m){
		
	}
	
	public void play(Message m, HostedConnection c){
		
	}
	
	@Broadcast(excludeSender=false)
	public boolean resend(Message m){
		return true;
	}
}
