package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.core.EntityManager;
import com.entity.network.core.items.WorldsScene;
import com.entity.network.core.msg.MsgListWorlds;
import com.entity.network.core.msg.MsgOnWorldCreatedSelected;
import com.jme3.network.MessageConnection;

public class WorldsMessageListener extends NetworkMessageListener<WorldsScene>{
	
	public void onListWorlds(MsgListWorlds msg, MessageConnection cnn)throws Exception{
		log.info("On list worlds: ");
		for(Object key:msg.worlds.keySet()){
			log.info("---->"+key);
		}

		getEntity().showWorldsList(msg);		
	}
	
	public void onWorldCreatedOrSelected(MsgOnWorldCreatedSelected msg, MessageConnection cnn) throws Exception{
		if(msg.error){			
			getEntity().service.setWorldDAO(null);
			if(msg.isCreated()){
				log.warning("Error creating world: The world already exists");
				throw new RuntimeException("The world already exists....");
			}else{
				log.warning("Error selecting world");
				throw new RuntimeException("Error selecting world....");
			}
		}else{
			log.info("World selected. Showing Client lobby");
			getEntity().showLobby();
		}
	}
	
}
