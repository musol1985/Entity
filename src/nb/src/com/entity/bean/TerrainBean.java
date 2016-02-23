package com.entity.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.adapters.TerrainFlatHeight;
import com.entity.anot.components.terrain.CustomHeightTerrain;
import com.entity.anot.components.terrain.ImageHeightTerrain;
import com.entity.anot.components.terrain.TerrainComponent;
import com.entity.anot.modificators.ApplyToComponent;
import com.entity.core.EntityManager;
import com.entity.core.IEntity;
import com.jme3.asset.AssetManager;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;

public class TerrainBean extends AnnotationFieldBean<TerrainComponent>{
	private Field heightField;
	private Method heightMethod;
	private String height;
	private String name;

	
	
	public TerrainBean(Field terrain, Class clas)throws Exception{
		super(terrain, TerrainComponent.class);
		
		if("".equals(annot.getName()))
			name=terrain.getName();
		
		if(EntityManager.isAnnotationPresent(ImageHeightTerrain.class, terrain)){
			height=EntityManager.getAnnotation(ImageHeightTerrain.class,terrain).image();
		}else{
			for(Field f:clas.getFields()){
				if(EntityManager.isAnnotationPresent(ApplyToComponent.class,f) && EntityManager.isAnnotationPresent(ImageHeightTerrain.class,f)){
					if(EntityManager.getAnnotation(ApplyToComponent.class,f).component().equals(terrain.getName())){
						heightField=f;
						height=EntityManager.getAnnotation(ImageHeightTerrain.class,f).image();
						break;
					}
				}
			}
			if(heightField==null){
				for(Method m:clas.getDeclaredMethods()){
					if(EntityManager.isAnnotationPresent(ApplyToComponent.class,m) && EntityManager.isAnnotationPresent(CustomHeightTerrain.class,m)){
						if(EntityManager.getAnnotation(ApplyToComponent.class,m).component().equals(terrain.getName())){
                            m.setAccessible(true);
							heightMethod=m;
							break;
						}
					}
				}
			}
		}
	}
	
	public void setHeight(Field f){
		this.heightField=f;
	}
	
	public void setHeight(Method m){
		this.heightMethod=m;
	}
	
	public boolean isHeightField(){
		return heightField!=null;
	}
	
	public boolean isHeightMethod(){
		return heightMethod!=null;
	}
	
	public AbstractHeightMap getHeight(IEntity e, AssetManager asset)throws Exception{
		if(isHeightField()){
			if(EntityManager.isAnnotationPresent(ImageHeightTerrain.class,heightField)){
				return getImageHeight(heightField, e, asset);
			}
		}else if(isHeightMethod()){
			return (AbstractHeightMap) heightMethod.invoke(e, f.get(e));
		}else if(height!=null){
			return getImageHeight(height, asset);
		}else{
            return new TerrainFlatHeight();
        }
		
		throw new Exception("Cant load height for terrain "+f.getName());
	}
	
	private AbstractHeightMap getImageHeight(Field f, IEntity e, AssetManager asset)throws Exception{
		ImageHeightTerrain anot=EntityManager.getAnnotation(ImageHeightTerrain.class,f);
		
		AbstractHeightMap heightmap = getImageHeight(anot.image(), asset);
	    
	    f.set(e, heightmap);
	    
	    return heightmap;
	}
	
	private AbstractHeightMap getImageHeight(String image, AssetManager asset)throws Exception{
		AbstractHeightMap heightmap = null;
	    Texture heightMapImage = asset.loadTexture(image);
	    heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
	    heightmap.load();
	    
	    return heightmap;
	}

	
	public String getName(){
		return name;
	}
}
