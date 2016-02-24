package com.entity.bean;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import com.entity.adapters.NetworkMessageListener;
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
	private static final Logger log = Logger.getLogger(MaterialBean.class.getName());
	
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
				component=clas.getField(EntityManager.getAnnotation(ApplyToComponent.class,material).component());
			}else{
				geometry=EntityManager.getAnnotation(ApplyToGeometry.class,material).geometry();
			}
			apply=true;
			if(anot.singleton()){
				mSingleton=EntityManager.getAssetManager().loadMaterial(anot.asset());
			}
		}else if(isComponent(material)){
			
			log.info("Material component!!!"+material.getName());
			component=material;
			apply=true;
			if(anot.singleton()){
				mSingleton=EntityManager.getAssetManager().loadMaterial(anot.asset());
			}
		}else{
			if(isGetFromComponent(material)){
				component=clas.getField(EntityManager.getAnnotation(ApplyToComponent.class,material).component());
			}else{
				geometry=EntityManager.getAnnotation(ApplyToGeometry.class,material).geometry();
			}
		}
	}
	
	public static boolean isApplyToComponent(Field f){
		return EntityManager.isAnnotationPresent(ApplyToGeometry.class,f);
	}
	
	public static boolean isGetFromComponent(Field f){
		return EntityManager.isAnnotationPresent(GetFromComponent.class,f);
	}
	
	public static boolean isApply(Field f){
		return EntityManager.isAnnotationPresent(ApplyToComponent.class,f) || EntityManager.isAnnotationPresent(ApplyToGeometry.class,f);
	}
	
	public static boolean isComponent(Field f){
		log.info(f.getDeclaringClass()+" "+f.getType());
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
			log.info("load mat "+component);
			if(component!=null){
				//It's a field based apply
				Object field=component.get(e);
				log.info("load mat field"+field);
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
