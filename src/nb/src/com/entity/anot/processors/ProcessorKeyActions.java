package com.entity.anot.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import com.entity.anot.components.input.Input;
import com.entity.anot.components.input.KeyInputMapping;


@SupportedAnnotationTypes(
		  {"com.entity.anot.input.Input"}
		)
public class ProcessorKeyActions extends AbstractProcessor{
	private Messager messager;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		try{
			messager = processingEnv.getMessager();

			List<String> inputs=new ArrayList<String>();
			
			for(Element action:roundEnv.getElementsAnnotatedWith(KeyInputMapping.class)){
				KeyInputMapping anot=action.getAnnotation(KeyInputMapping.class);
				if(anot!=null){
					inputs.add(anot.action());
				}
			}
			
			for (TypeElement te : annotations) {
				for(Element action:roundEnv.getElementsAnnotatedWith(te)){
					Input anot=action.getAnnotation(Input.class);
					if(anot!=null){
						if(!inputs.contains(anot.action())){
							messager.printMessage(Diagnostic.Kind.ERROR, "The action "+anot.action()+" doesn't exist in an Input annotation.",action);
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
