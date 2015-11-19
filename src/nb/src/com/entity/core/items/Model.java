package com.entity.core.items;

import java.io.IOException;
import java.lang.reflect.Field;

import com.entity.anot.BuilderDefinition;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.builders.ModelBuilder;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

@BuilderDefinition(builderClass=ModelBuilder.class)
public abstract class Model extends Node implements IEntity{
	private ModelBuilder builder;
	
	protected boolean isCollidableWith(Model e){
		return false;
	}



	@Override
	public void onAttachToParent(IEntity parent) throws Exception {
		
	}



	@Override
	public Node getNode() {
		return this;
	}



	@Override
	public void attachToParent(IEntity parent) throws Exception {
		if(parent.getNode()==null)
			throw new Exception(getClass().getName()+" Cannot attach to a "+parent.getClass().getName());
		
		builder.onAttachInstance(this);
		
		parent.getNode().attachChild(this);
		onAttachToParent(parent);
	}

    @Override
    public void onInstance(IBuilder builder){
    }
    
    @Override
    public void setBuilder(IBuilder builder) {
        this.builder=(ModelBuilder) builder;
    }


	@Override
	public void dettach() throws Exception {
		if(getParent()!=null){
			IEntity parent=(IEntity) getParent();
			
			builder.onDettachInstance(this);
			
			getParent().detachChild(this);
			onDettach(parent);
		}
	}

	@Override
	public void attachChilFromInjector(Spatial s) {
		attachChild(s);
	}    

    @Override
    public void onDettach(IEntity parent)throws Exception{
        
    }



	@Override
	public void read(JmeImporter i) throws IOException {
		super.read(i);
		
		InputCapsule data=i.getCapsule(this);
		for(Field f:builder.getDaoFields()){
			try{
				if(f.getType().isAssignableFrom(Savable.class)){
					f.set(this, data.readSavable(f.getName(), null));
				}else if(f.getType().isAssignableFrom(Integer.class)){
					f.set(this, data.readInt(f.getName(), 0));					
				}else if(f.getType().isAssignableFrom(Float.class)){
					f.set(this, data.readFloat(f.getName(), 0.0f));
				}else if(f.getType().isAssignableFrom(String.class)){
					f.set(this, data.readString(f.getName(), ""));
				}
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
	}



	@Override
	public void write(JmeExporter e) throws IOException {
		super.write(e);
		
		OutputCapsule data=e.getCapsule(this);
		for(Field f:builder.getDaoFields()){
			try{
				if(f.getType().isAssignableFrom(Savable.class)){
					data.write((Savable)f.get(this), f.getName(), null);
				}else if(f.getType().isAssignableFrom(Integer.class)){
					data.write((Integer)f.get(this), f.getName(), 0);
				}else if(f.getType().isAssignableFrom(Float.class)){
					data.write((Float)f.get(this), f.getName(), 0.0f);
				}else if(f.getType().isAssignableFrom(String.class)){
					data.write((String)f.get(this), f.getName(), "");
				}
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
	}
	
	
}	
