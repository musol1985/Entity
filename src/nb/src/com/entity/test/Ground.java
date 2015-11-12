package com.entity.test;

import com.entity.anot.components.model.MaterialComponent;
import com.entity.anot.components.terrain.CustomHeightTerrain;
import com.entity.anot.components.terrain.ImageHeightTerrain;
import com.entity.anot.components.terrain.TerrainComponent;
import com.entity.anot.entities.ModelEntity;
import com.entity.anot.modificators.ApplyToComponent;
import com.entity.core.items.Model;
import com.jme3.material.Material;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.heightmap.AbstractHeightMap;

@ModelEntity
public class Ground extends Model{
	@TerrainComponent
	@ImageHeightTerrain(image="Textures/height.jpg")
	@MaterialComponent(asset="Materials/terrain.j3m")
	private Terrain terrain;
	
	@TerrainComponent()
	private Terrain terrain2;
	
	@MaterialComponent(asset="Materials/terrain2.j3m")
	@ApplyToComponent(component="terrain2")
	private Material material2;
	
	@TerrainComponent()
	private Terrain terrain3;
	
	@ImageHeightTerrain(image="Textures/height2.jpg")
	@ApplyToComponent(component="terrain2")
	private AbstractHeightMap height;
	
	@CustomHeightTerrain
	@ApplyToComponent(component="terrain3")
	private AbstractHeightMap generateHeight(Terrain t){
		return new AbstractHeightMap() {			
			@Override
			public boolean load() {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}
}
