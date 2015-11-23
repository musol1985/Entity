package com.entity.bean.custom;

import java.lang.reflect.Field;

import com.entity.anot.components.model.MaterialComponent;
import com.entity.anot.modificators.ApplyToComponent;
import com.entity.anot.modificators.ApplyToGeometry;
import com.entity.anot.modificators.GetFromComponent;
import com.entity.bean.AnnotationFieldBean;
import com.entity.core.EntityManager;
import com.entity.core.items.Model;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
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
				component=clas.getField(material.getAnnotation(ApplyToComponent.class).component());
			}else{
				geometry=material.getAnnotation(ApplyToGeometry.class).geometry();
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
				m=EntityManager.getAssetManager().loadMaterial(annot.asset());
			}
			if(f.getType()==Material.class)
				f.set(e, m);

			if(component!=null){
				//It's a field based apply
				Object field=component.get(e);
				if(field==null)
					throw new Exception("Cannot apply material to component. Field "+component.getName()+" is null. Verify the injector order!!!");

				if(field instanceof TerrainQuad){
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
				m=((Geometry)e.getChild(geometry)).getMaterial();
			}
			f.set(e, m);
		}
	}
	
}
