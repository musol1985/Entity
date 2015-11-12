package com.entity.anot.processors;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;


@SupportedAnnotationTypes(
		  {"com.entity.anot.OnCollision"}
		)
public class ProcessorOnCollision extends AbstractProcessor{
	private Messager messager;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		try{
			messager = processingEnv.getMessager();
			for (TypeElement te : annotations) {
			      for (Element e : roundEnv.getElementsAnnotatedWith(te)) {
			    	  if(e.getKind()==ElementKind.METHOD){
			    		  
			    		  ExecutableElement metodo=(ExecutableElement)e;
				    		  if(metodo.getParameters().size()<1 || metodo.getParameters().size()>2){
				    			  messager.printMessage(Diagnostic.Kind.ERROR, "Error in parameters size of "+e.getSimpleName()+". Size must be 1 or 2.",e);
				    		  }else{
				    			  if(metodo.getParameters().size()==1){
				    				  VariableElement v=metodo.getParameters().get(0);
				    				  if(!(implementa(v.asType(), "com.entity.core.items.Model")||implementa(v.asType(), "com.jme3.bullet.collision.PhysicsCollisionEvent"))){
				    					  messager.printMessage(Diagnostic.Kind.ERROR, v.getSimpleName()+" must implement Entity or PhysicsCollisionEvent",v);
				    				  }	
				    			  }else{
					    			  for(VariableElement v:metodo.getParameters()){
					    				  if(!(implementa(v.asType(), "com.entity.core.items.Model")||implementa(v.asType(), "com.jme3.bullet.collision.PhysicsCollisionEvent"))){
					    					  messager.printMessage(Diagnostic.Kind.ERROR, v.getSimpleName()+" must implement Entity or PhysicsCollisionEvent",v);
					    				  }	 	
						    		  } 
				    			  }
				    		  }	
			    	  }
			      }
		    }
		}catch(Exception e){
			e.printStackTrace();
			messager.printMessage(Diagnostic.Kind.ERROR,
	                e.getMessage());
		}
		return true;
	}
	
	 @Override
	  public SourceVersion getSupportedSourceVersion() {
	    return SourceVersion.latestSupported();
	  }

	 
	 private boolean implementa(TypeMirror type, String clase){
		 if(type.toString().equals(clase))
			 return true;
		 
		 for(TypeMirror padre:processingEnv.getTypeUtils().directSupertypes(type)){
			 if(padre.toString().equals(clase))
				 return true;			  
		  }
		 return false;
	 }
}
