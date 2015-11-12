package com.entity.bean;

import java.lang.reflect.Field;

import com.entity.anot.components.model.MaterialComponent;
import com.entity.anot.modificators.ApplyToComponent;
import com.entity.anot.modificators.ApplyToGeometry;
import com.entity.anot.modificators.GetFromComponent;
import com.entity.core.EntityManager;
import com.entity.core.items.Model;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;

public class MaterialBean {
	private boolean apply;
	
	private Field material;
	
	private Field component;
	private String geometry;


	private MaterialComponent anot;
	private Material mSingleton;
	
	
	public MaterialBean(Field material, Class clas, MaterialComponent anot)throws Exception{
		this.material=material;
		this.anot=anot;

		if(isApply(material)){
			if(isApplyToComponent(material)){				
				component=clas.getField(material.getAnnotation(ApplyToComponent.class).component());
			}else{
				geometry=material.getAnnotation(ApplyToGeometry.class).geometry();
			}
			apply=true;
			if(anot.singleton()){
				mSingleton=EntityManager.getAssetManager().loadMaterial(anot.asset());
			}
		}else if(isComponent(material)){
			System.out.println("Material component!!!"+material.getName());
			component=material;
			apply=true;
			if(anot.singleton()){
				mSingleton=EntityManager.getAssetManager().loadMaterial(anot.asset());
			}
		}else{
			if(isGetFromComponent(material)){
				component=clas.getField(material.getAnnotation(ApplyToComponent.class).component());
			}else{
				geometry=material.getAnnotation(ApplyToGeometry.class).geometry();
			}
		}
	}
	
	public static boolean isApplyToComponent(Field f){
		return f.isAnnotationPresent(ApplyToGeometry.class);
	}
	
	public static boolean isGetFromComponent(Field f){
		return f.isAnnotationPresent(GetFromComponent.class);
	}
	
	public static boolean isApply(Field f){
		return f.isAnnotationPresent(ApplyToComponent.class) || f.isAnnotationPresent(ApplyToGeometry.class);
	}
	
	public static boolean isComponent(Field f){
		System.out.println(f.getDeclaringClass()+" "+f.getType());
		return f.getType()==Geometry.class || f.getType()==TerrainQuad.class;
	}
	
	public void onLoad(Model e)throws Exception{
		if(apply){
			Material m=mSingleton;
			if(m==null){
				m=EntityManager.getAssetManager().loadMaterial(anot.asset());
			}
			if(material.getType()==Material.class)
				material.set(e, m);
			System.out.println("load mat "+component);
			if(component!=null){
				//It's a field based apply
				Object field=component.get(e);
				System.out.println("load mat field"+field);
				if(field instanceof TerrainQuad){
					((TerrainQuad) field).setMaterial(m);
				}else if(field instanceof Spatial){
					((Spatial) field).setMaterial(m);
				}else{
					throw new Exception("Field type "+material.getType().getName()+" not supported for set Material");
				}
			}else{
				e.getChild(geometry).setMaterial(m);
			}
		}else{
			Material m=null;
			if(component!=null){
				Object field=component.get(e);
				if(field instanceof TerrainQuad){
					m=((TerrainQuad) field).getMaterial();
				}else if(field instanceof Spatial){
					m=((Geometry) field).getMaterial();
				}else{
					throw new Exception("Field type "+material.getType().getName()+" not supported for set Material");
				}
			}else{
				m=((Geometry)e.getChild(geometry)).getMaterial();
			}
			material.set(e, m);
		}
	}
	
}
