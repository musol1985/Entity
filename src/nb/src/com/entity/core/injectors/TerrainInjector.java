package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.anot.components.terrain.TerrainComponent;
import com.entity.bean.TerrainBean;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;

public class TerrainInjector<T extends IEntity>  extends BaseInjector<T>{
	private List<TerrainBean> terrains=new ArrayList<TerrainBean>();

	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(TerrainBean t:terrains){
			TerrainQuad terrain = new TerrainQuad(t.getName(), t.getAnot().chunkSize()+1, t.getAnot().realSize()+1, t.getHeight(e, EntityManager.getAssetManager()).getHeightMap());
			System.out.println("-------------------------------------------------------->Terrain");
			if(t.getAnot().LOD()){
				terrain.addControl(new TerrainLodControl(terrain, EntityManager.getCamera()));
			}
			
			t.getTerrainField().set(e, terrain);
			
			if(t.getAnot().attach()){			
				e.attachChilFromInjector(terrain);
			}
		}
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(TerrainComponent.class)){
			f.setAccessible(true);
			terrains.add(new TerrainBean(f, c, f.getAnnotation(TerrainComponent.class)));
		}
	}

	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return terrains.size()>0;
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
