package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.CamNode;
import com.entity.anot.network.NetSync;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityGame;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.InjectorAttachable;
import com.entity.core.injectors.BaseInjector;
import com.entity.core.injectors.ListBeanInjector;
import com.entity.core.items.NetworkModel;
import com.entity.core.items.Scene;
import com.entity.network.core.msg.sync.FieldSync;

public class SyncMessageInjector<T extends NetworkModel>  extends ListBeanInjector<AnnotationFieldBean<NetSync>, T> implements InjectorAttachable<T>{
	
	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(NetSync.class,f)){
			beans.add(new AnnotationFieldBean<NetSync>(f, NetSync.class));
		}
	}

	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		
	}
	
    @Override
    public int compareTo(BaseInjector t) {
        return 1;
    }

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance)
			throws Exception {
		for(AnnotationFieldBean<NetSync> bean: beans){
			bean.getField().set(instance, bean.getField().getType().newInstance());
			
			FieldSync field=new FieldSync(bean, instance);
			
			Scene s=EntityManager.getCurrentScene();
			if(s!=null){
				s.getNetSync().addField(field);
			}
		}
		
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance)
			throws Exception {
		for(AnnotationFieldBean<NetSync> bean: beans){
			Scene s=EntityManager.getCurrentScene();
			if(s!=null){
				s.getNetSync().removeField(FieldSync.getID(instance, bean));
			}
		}
	}
}
