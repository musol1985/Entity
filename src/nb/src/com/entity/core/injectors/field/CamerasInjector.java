package com.entity.core.injectors.field;

import java.lang.reflect.Field;

import com.entity.adapters.FollowCameraAdapter;
import com.entity.adapters.ScrollCameraAdapter;
import com.entity.anot.FollowCameraNode;
import com.entity.anot.ScrollCameraNode;
import com.entity.bean.custom.CameraBean;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.injectors.ListBeanInjector;
import com.entity.core.items.ModelBase;

public class CamerasInjector<T extends IEntity>  extends ListBeanInjector<CameraBean, T>{
	
	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(CameraBean.isScrollCamera(f)){
			beans.add(new CameraBean(c, f, ScrollCameraNode.class));
		}else if(CameraBean.isFollowCamera(f)){
			beans.add(new CameraBean(c, f, FollowCameraNode.class));
		}
	}

	@Override
	public void onInstance(final T e, IBuilder builder, Object[] params) throws Exception {
		for(CameraBean bean:beans){
			if(bean.isScrollCamera()){
				ScrollCameraNode anot=(ScrollCameraNode)bean.getAnnot();
				ScrollCameraAdapter entity=(ScrollCameraAdapter)bean.instanceEntity();
				
				entity.onInstance(builder, null); 

				if(anot.debug())
					entity.debug(true);
				
				bean.getField().set(e, entity);
				
				entity.setValues(anot);
				
				if(anot.attach())
					entity.attachToParent(e);
			}else if(bean.isFollowCamera()){
				FollowCameraNode anot=(FollowCameraNode)bean.getAnnot();
				FollowCameraAdapter entity=(FollowCameraAdapter)bean.instanceEntity();
				
				entity.onInstance(builder, null); 

				if(anot.debug())
					entity.debug(true);
				
				bean.getField().set(e, entity);
				
				entity.setValues(anot);
				
				if(!anot.followTo().isEmpty()){
					Field f=e.getClass().getField(anot.followTo());
					if(f==null)
						throw new RuntimeException("No field follow to in class"+e.getClass().getName());
					f.setAccessible(true);
					
					entity.followTo((ModelBase)f.get(e));
				}
				
				if(!bean.hasListener()){
					entity.setListener(bean.getListener(e));
				}
				
				if(anot.attach())
					entity.attachToParent(e);
			}
			
			
		}
	}
}
