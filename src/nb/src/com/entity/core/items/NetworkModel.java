package com.entity.core.items;

import com.entity.anot.BuilderDefinition;
import com.entity.anot.network.NetSync;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.builders.NetworkModelBuilder;
import com.entity.core.injectors.field.SyncMessageInjector;
import com.entity.network.FieldSync;

@BuilderDefinition(builderClass=NetworkModelBuilder.class)
public abstract class NetworkModel extends Model{
	private boolean controlled;
	
	public String getNetID(){
		return "EntityNet#"+getName();
	}
	
	public void netControl(){
		if(!controlled){
			Scene s=EntityManager.getCurrentScene();
			if(s!=null){
				SyncMessageInjector<NetworkModel> injector=(SyncMessageInjector<NetworkModel>) builder.getInjector(SyncMessageInjector.class);
				for(AnnotationFieldBean<NetSync> bean:injector.getBeans()){
					s.getNetSync().controlField(FieldSync.getID(this, bean));
				}				
				controlled=true;
			}
		}
	}
	
	public void netUnControl(){
		if(controlled){
			Scene s=EntityManager.getCurrentScene();
			if(s!=null){
				SyncMessageInjector<NetworkModel> injector=(SyncMessageInjector<NetworkModel>) builder.getInjector(SyncMessageInjector.class);
				for(AnnotationFieldBean<NetSync> bean:injector.getBeans()){
					s.getNetSync().unControlField(FieldSync.getID(this, bean));
				}		
				controlled=false;
			}
		}
	}
	
	
}	
