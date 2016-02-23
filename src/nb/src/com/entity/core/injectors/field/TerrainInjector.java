package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.anot.CamNode;
import com.entity.anot.components.terrain.TerrainComponent;
import com.entity.bean.TerrainBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;

public class TerrainInjector<T extends IEntity>  extends ListBeanInjector<TerrainBean, T>{
	
	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(EntityManager.isAnnotationPresent(TerrainComponent.class,f)){
			beans.add(new TerrainBean(f, c));
		}
	}

	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(TerrainBean bean:beans){
			TerrainQuad terrain = new TerrainQuad(bean.getName(), bean.getAnnot().chunkSize()+1, bean.getAnnot().realSize()+1, bean.getHeight(e, EntityManager.getAssetManager()).getHeightMap());

			if(bean.getAnnot().LOD()){
				terrain.addControl(new TerrainLodControl(terrain, EntityManager.getCamera()));
			}
			
			bean.getField().set(e, terrain);
			
			if(bean.getAnnot().attach()){			
				e.attachChilFromInjector(terrain);
			}
		}
	}

}
