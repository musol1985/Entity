package com.entity.bean.custom;

import java.lang.reflect.Field;

import com.entity.anot.components.model.MaterialComponent;
import com.entity.anot.modificators.ApplyToComponent;
import com.entity.anot.modificators.ApplyToGeometry;
import com.entity.anot.modificators.GetFromComponent;
import com.entity.anot.modificators.GetFromGeometry;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.items.Model;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainQuad;

public class MaterialBean extends AnnotationFieldBean<MaterialComponent>{
	private boolean apply;
	
	private Field component;
	private String geometry;

	private Material mSingleton;
	
	
	public MaterialBean(Field material, Class clas, Class<MaterialComponent> anot)throws Exception{
		super(material, anot);


		if(isApply(material)){
			if(isApplyToComponent(material)){				
				component=clas.getField(EntityManager.getAnnotation(ApplyToComponent.class,material).component());
			}else{
				geometry=EntityManager.getAnnotation(ApplyToGeometry.class,material).geometry();
			}
			apply=true;
			if(annot.singleton()){
				mSingleton=EntityManager.getAssetManager().loadMaterial(annot.asset());
			}
		}else if(isComponent(material)){
			component=material;
			apply=true;
			if(annot.singleton()){
				mSingleton=EntityManager.getAssetManager().loadMaterial(annot.asset());
			}
		}else{
			if(isGetFromComponent(material)){
				component=clas.getField(EntityManager.getAnnotation(GetFromComponent.class,material).component());
			}else{
                            if(material.getType()!=Material.class){
				geometry=EntityManager.getAnnotation(GetFromGeometry.class,material).geometry();
                            }
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
		return f.getType()==Geometry.class || f.getType()==TerrainQuad.class || f.getType()==Terrain.class;
	}
	
	public void onLoad(Model e)throws Exception{
		if(apply){
			Material m=mSingleton;
			if(m==null){
				m=EntityManager.getAssetManager().loadMaterial(annot.asset());
			}
			if(f.getType()==Material.class)
				f.set(e, m);

			if(component!=null){
				//It's a field based apply
				Object field=component.get(e);
				if(field==null)
					throw new Exception("Cannot apply material to component. Field "+component.getName()+" is null. Verify the injector order!!!");

                                if(field instanceof Terrain){
					((TerrainQuad) field).setMaterial(m);
				}else if(field instanceof TerrainQuad){
					((TerrainQuad) field).setMaterial(m);
				}else if(field instanceof Spatial){
					((Spatial) field).setMaterial(m);
				}else{
					throw new Exception("Field type "+f.getType().getName()+" not supported for set Material");
				}
			}else{
				e.getChild(geometry).setMaterial(m);
			}
		}else{
			Material m=null;
			if(component!=null){
				Object field=component.get(e);
				
				if(field==null)
					throw new Exception("Cannot apply material to component. Field "+component.getName()+" is null. Verify the injector order!!!");
				
				if(field instanceof TerrainQuad){
					m=((TerrainQuad) field).getMaterial();
				}else if(field instanceof Spatial){
					m=((Geometry) field).getMaterial();
				}else{
					throw new Exception("Field type "+f.getType().getName()+" not supported for set Material");
				}
			}else{
                            if(geometry!=null){
				m=((Geometry)e.getChild(geometry)).getMaterial();
                            }else{
                                m=EntityManager.getAssetManager().loadMaterial(annot.asset());
                            }
			}
			f.set(e, m);
		}
	}
	
}
