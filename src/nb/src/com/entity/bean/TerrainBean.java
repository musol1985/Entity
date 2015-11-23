package com.entity.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.entity.adapters.TerrainFlatHeight;
import com.entity.anot.components.terrain.CustomHeightTerrain;
import com.entity.anot.components.terrain.ImageHeightTerrain;
import com.entity.anot.components.terrain.TerrainComponent;
import com.entity.anot.modificators.ApplyToComponent;
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
		
		if(terrain.isAnnotationPresent(ImageHeightTerrain.class)){
			height=terrain.getAnnotation(ImageHeightTerrain.class).image();
		}else{
			for(Field f:clas.getFields()){
				if(f.isAnnotationPresent(ApplyToComponent.class) && f.isAnnotationPresent(ImageHeightTerrain.class)){
					if(f.getAnnotation(ApplyToComponent.class).component().equals(terrain.getName())){
						heightField=f;
						height=f.getAnnotation(ImageHeightTerrain.class).image();
						break;
					}
				}
			}
			if(heightField==null){
				for(Method m:clas.getDeclaredMethods()){
					if(m.isAnnotationPresent(ApplyToComponent.class) && m.isAnnotationPresent(CustomHeightTerrain.class)){
						if(m.getAnnotation(ApplyToComponent.class).component().equals(terrain.getName())){
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
			if(heightField.isAnnotationPresent(ImageHeightTerrain.class)){
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
		ImageHeightTerrain anot=f.getAnnotation(ImageHeightTerrain.class);
		
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
