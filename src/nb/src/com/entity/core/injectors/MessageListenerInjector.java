package com.entity.core.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.entity.adapters.NetworkMessageListener;
import com.entity.anot.network.MessageListener;
import com.entity.bean.SingletonBean;
import com.entity.core.EntityGame;
import com.entity.core.IBuilder;
import com.entity.core.IEntity;
import com.entity.core.Injector;
import com.entity.core.InjectorAttachable;

public class MessageListenerInjector<T extends IEntity>  extends BaseInjector<T> implements InjectorAttachable<T>{
	private List<Field> fields=new ArrayList<Field>();
	private List<SingletonBean> singletons=new ArrayList<SingletonBean>();


	@Override
	public void onInstance(final T e, IBuilder builder) throws Exception {
		for(Field f:fields){				
			f.set(e, f.getType().newInstance());
		}
		for(SingletonBean singleton:singletons){
			singleton.getF().set(e, singleton.getInstance());
		}
	}

	@Override
	public void loadField(Class<T> c, Field f) throws Exception {
		if(f.isAnnotationPresent(MessageListener.class)){
			f.setAccessible(true);
			if(f.getAnnotation(MessageListener.class).singleton()){
				singletons.add(new SingletonBean(f.getType().newInstance(), f));
			}else{
				fields.add(f);
			}
		}
	}
	



	@Override
	public void loadMethod(Class<T> c, Method m) throws Exception {

	}

	@Override
	public boolean hasInjections() {
		return singletons.size()>0 || fields.size()>0;
	}

	@Override
	public <G extends EntityGame> void onAttach(G app, T instance)throws Exception {
		for(Field f:fields){
			app.addMessageListener((NetworkMessageListener) f.get(instance));
		}
		for(SingletonBean singleton:singletons){
			app.addMessageListener((NetworkMessageListener)singleton.getInstance());
		}
	}

	@Override
	public <G extends EntityGame> void onDettach(G app, T instance)throws Exception{
		for(Field f:fields){
			app.removeMessageListener((NetworkMessageListener) f.get(instance));
		}
		for(SingletonBean singleton:singletons){
			app.removeMessageListener((NetworkMessageListener)singleton.getInstance());
		}
	}

	@Override
	public void onCreate(Class<T> c) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
