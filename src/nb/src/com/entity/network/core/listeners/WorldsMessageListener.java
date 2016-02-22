package com.entity.network.core.listeners;

import com.entity.adapters.NetworkMessageListener;
import com.entity.core.EntityManager;
import com.entity.network.core.bean.NetPlayer;
import com.entity.network.core.bean.NetWorld;
import com.entity.network.core.items.LobbyClientScene;
import com.entity.network.core.items.WorldsScene;
import com.entity.network.core.msg.MsgCreateWorld;
import com.entity.network.core.msg.MsgListWorlds;
import com.entity.network.core.msg.MsgOnNewPlayer;
import com.entity.network.core.msg.MsgOnWorldCreatedSelected;
import com.entity.network.core.msg.MsgSelectWorld;
import com.jme3.network.HostedConnection;

public class WorldsMessageListener extends NetworkMessageListener<WorldsScene>{
	
	public void onListWorlds(MsgListWorlds msg, HostedConnection cnn)throws Exception{
		System.out.println("on list worlds");
		//TODO show on GUI the worlds & create World(with options)
		NetWorld world=null;
		
		if(msg.worlds.size()==0){
			//Creamos un mundo
			world=getEntity().createWorld();
			world.setCreated(true);
			world.setPlayerCreator(getEntity().getPlayerName());
			new MsgCreateWorld(world).send();
		}else{
			//Seleccionamos el 0 por defecto
			world=(NetWorld) msg.worlds.get(0);
			new MsgSelectWorld(world.getId()).send();
			
		}
		EntityManager.getGame().getNet().setWorld(world);
	}
	
	public void onWorldCreatedOrSelected(MsgOnWorldCreatedSelected msg, HostedConnection cnn) throws Exception{
		if(msg.error){
			EntityManager.getGame().getNet().setWorld(null);
			if(msg.isCreated()){
				throw new RuntimeException("El mundo ya existe....");
			}else{
				throw new RuntimeException("Error al seleccionar el mundo....");
			}
		}else{
			getEntity().showLobby();
			//new MsgOnNewPlayer(getEntity().getPlayerName(), true).send();
		}
	}
	
}
