package com.entity.core.items;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Logger;

import com.entity.adapters.AutoBatchNode;
import com.entity.core.EntityManager;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.builders.BaseModelBuilder;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public abstract class ModelBase<R extends ModelBase, T extends BaseModelBuilder> extends Node implements IEntity{
	protected static final Logger log = Logger.getLogger(ModelBase.class.getName());
	protected T builder;
	
	protected boolean isCollidableWith(ModelBase e){
		return false;
	}
	
	@Override
	public Node getNode() {
		return this;
	}

	@Override
	public void attachToParent(IEntity parent) throws Exception {
		if(parent.getNode()==null)
			throw new Exception(getClass().getName()+" Cannot attach to a "+parent.getClass().getName());
				
		
		parent.getNode().attachChild(this);
		
		//Call to all the childs and their childs injector attached event
		onAParentAttached(parent);
		
		onAttachToParent(parent);
	}
	
	public R getParentModel(){
            Spatial s=getParent();
            if(s instanceof AutoBatchNode)
                return (R)((AutoBatchNode)s).getUserData(BatchModel.BATCH_MODEL_REFERENCE);
            
            return (R)s;
	}


    
    @Override
    public void setBuilder(IBuilder builder) {
        this.builder=(T) builder;
    }


	@Override
	public void dettach() throws Exception {
		if(getParent()!=null){
			IEntity parent=(IEntity) getParent();
			
			getParent().detachChild(this);	
			
			//Call to all the childs and their childs injector dettached event
			onAParentDettached(parent);
			
			onDettach(parent);
		}
	}
	
	public boolean isAttached(){
		return getParent()!=null;
	}

	/**
	 * Called when a parent of the tree(this parent's, or his parent, etc) has been dettached
	 * @throws Exception
	 */
	public void onAParentDettached(IEntity parent) throws Exception {
		builder.onDettachInstance(this);	
		//Notify my ModelBase children that a parent has been dettached
		for(Spatial s:getChildren()){
        	if(s instanceof ModelBase){
        		((ModelBase) s).onAParentDettached(this);
        	}
        }
	}
	
	
	/**
	 * Called when a parent of the tree has been attached
	 * @throws Exception
	 */
	public void onAParentAttached(IEntity parent) throws Exception {
		builder.onAttachInstance(this);
		//Notify my ModelBase children that a parent has been attached
		for(Spatial s:getChildren()){
        	if(s instanceof ModelBase){
        		((ModelBase) s).onAParentAttached(this);
        	}
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
		for(Object o:builder.getDaoFields()){
			Field f=(Field)o;
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
		for(Object o:builder.getDaoFields()){
			Field f=(Field)o;
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

	@Override
	public void onInstance(IBuilder builder, Object[] params) throws Exception{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttachToParent(IEntity parent) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPreInject(IBuilder builder, Object[] params) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Puts this model in the cache system
	 */
	public void cache(){
		if(!builder.isCache())
			throw new RuntimeException("The builder has not @Cache");
		
		EntityManager.getGame().putInCache(this);
	}

	@Override
	public void onInstanceCache(IBuilder builder, Object[] params)
			throws Exception {
		
	}
	
	public Vector3f getRotation(){
		float[] v=(getLocalRotation().toAngles(new float[3]));
		return new Vector3f(v[0], v[1], v[2]);
	}
}	
