package com.entity.core.interceptors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import net.sf.cglib.proxy.MethodProxy;

import com.entity.anot.RayPick;
import com.entity.core.EntityManager;
import com.entity.core.builders.ModelBuilder;
import com.entity.core.items.Model;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainPatch;
import com.jme3.terrain.geomipmap.TerrainQuad;

public class RayPickInterceptor {
	
	protected static final Logger log = Logger.getLogger(RayPickInterceptor.class.getName());
	
	
	public static CollisionResults rayClickCollisionResult(String body)throws Exception{
		Vector3f pos=EntityManager.getCamera().getWorldCoordinates(EntityManager.getInputManager().getCursorPosition(), 0).clone();
        Vector3f dir=EntityManager.getCamera().getWorldCoordinates(EntityManager.getInputManager().getCursorPosition(), 0.3f).clone();
        dir.subtractLocal(pos).normalizeLocal();
        Ray ray=new Ray(pos, dir);
        
        Node n=EntityManager.getCurrentScene().getApp().getRootNode();
        if(!body.isEmpty()){
        	n=(Node)n.getChild(body);
        }
        
        CollisionResults results=new CollisionResults();
        n.collideWith(ray, results);
        
        return results;
  
	}

	public static Object rayPick(Object obj, Method m, Object[] args, MethodProxy mp, BaseMethodInterceptor mi)throws Exception, Throwable{
		RayPick anot=EntityManager.getAnnotation(RayPick.class,m);
		
		if(args.length>3)
			throw new Exception("Only 3 args max allowed on RayPick method.");
		
		Node n=EntityManager.getCurrentScene().getApp().getRootNode();
		
		if(anot.NodeName().length()>0){
			n=(Node)n.getChild(anot.NodeName());
		}else if(anot.Node().length()>0){
			Field f=obj.getClass().getSuperclass().getDeclaredField(anot.Node());
			f.setAccessible(true);
		    n=(Node)f.get(obj);
		}
		
		
		CollisionResults results=new CollisionResults();
		
		int argsRay=-1, argsEntity=-1, argsCollisionResults=-1, argsCollisionResult=-1;
		
		for(int i=0;i<m.getParameterTypes().length;i++){
			if(m.getParameterTypes()[i]==Ray.class){
				argsRay=i;
			}else if(m.getParameterTypes()[i]==CollisionResults.class){
				argsCollisionResults=i;
			}else if(m.getParameterTypes()[i]==CollisionResult.class){
				argsCollisionResult=i;
			}else if(Model.class.isAssignableFrom(m.getParameterTypes()[i])){
				argsEntity=i;				
			}
                    
		}
                
        Ray ray=null;
        
        if(argsRay==-1){
        	log.info("Throwing a RayPick from the camera in "+n.getName());      
            Vector3f pos=EntityManager.getCamera().getWorldCoordinates(EntityManager.getInputManager().getCursorPosition(), 0).clone();
            Vector3f dir=EntityManager.getCamera().getWorldCoordinates(EntityManager.getInputManager().getCursorPosition(), 0.3f).clone();
            dir.subtractLocal(pos).normalizeLocal();
            ray=new Ray(pos, dir);
        }else{
           ray=(Ray) args[argsRay]; 
        }

		n.collideWith(ray, results);
		
		Object[] argsProcesed=new Object[args.length];
        if(argsRay>-1)
            argsProcesed[argsRay]=ray;
		
		if(argsCollisionResults>-1)
			argsProcesed[argsCollisionResults]=results;
	
		CollisionResults resultsFiltered=null;
		if(anot.EntityFilter().length>0 && argsEntity==-1)
			resultsFiltered=new CollisionResults();                                
		
		for(CollisionResult colision:results){
			if(argsEntity>-1){
				log.info("Collision with "+colision.getGeometry().getName());                            
				Model entity=getEntityByGeometry(colision.getGeometry());
                                if(entity!=null){
                                    Class entityClass=EntityManager.getClass(entity.getClass());
                                    
                                    if(entity!=null && argsEntity>-1 && m.getParameterTypes()[argsEntity]==entityClass){
                                            argsProcesed[argsEntity]=entity;
                                            if(argsCollisionResult>-1)
                                                argsProcesed[argsCollisionResult]=colision;
                                            Object res=mi.callSuper(obj, m, mp,argsProcesed);
                                            if(res!=null)
                                                    return res;
                                    }
                                }
			}else{
				if(anot.EntityFilter().length==0){
					Object res=mi.callSuper(obj, m, mp,argsProcesed);
					if(res!=null)
						return res;
				}else{
					Model entity=getEntityByGeometry(colision.getGeometry());
					if(entity!=null && hasEntityClass(anot.EntityFilter(), entity)){
						resultsFiltered.addCollision(colision);
					}
				}
			}
		}
		
		if(resultsFiltered!=null && resultsFiltered.size()>0){
			if(argsRay==0){
				argsProcesed[1]=resultsFiltered;
			}else{
				argsProcesed[0]=resultsFiltered;
			}
			
			return mi.callSuper(obj, m, mp,argsProcesed);
		}
		return null;
	}

	
	public static Model getEntityByGeometry(Geometry geo){
		Model res=null;
		
        if(geo instanceof TerrainPatch){
            res=(Model) getSpatialByTerrain((TerrainPatch) geo);
        }else{
            res=geo.getUserData(ModelBuilder.ENTITY_GEOMETRY_REFERENCE);
        }
                
		
		if(res==null && geo.getParent()!=null)
			res=geo.getParent().getUserData(ModelBuilder.ENTITY_MODEL_REFERENCE);
		
		return res;
	}
        
        private static Spatial getSpatialByTerrain(TerrainPatch t){
            Spatial res=t.getParent();
            while(res instanceof TerrainPatch || res instanceof TerrainQuad){
                res=res.getParent();
            }
            return res;
        }
	
	private static boolean hasEntityClass(Class[] list, Model e){
		for(Class c:list){
			if(c==e.getClass())
				return true;
		}
		return false;
	}
}
