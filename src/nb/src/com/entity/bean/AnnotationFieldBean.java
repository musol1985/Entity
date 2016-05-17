package com.entity.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.logging.Logger;

import org.w3c.dom.TypeInfo;

import com.entity.core.EntityManager;

public class AnnotationFieldBean<T extends Annotation> {
	private static final Logger log = Logger.getLogger(AnnotationFieldBean.class.getName());
	protected Field f;
	protected T annot;
	
	public AnnotationFieldBean(Field f, Class<T> annotationClass)throws Exception{
		f.setAccessible(true);
		this.f=f;
		this.annot=EntityManager.getAnnotation(annotationClass,f);
	}
	
	public AnnotationFieldBean(Field f, T annotationClass)throws Exception{
		f.setAccessible(true);
		this.f=f;
		this.annot=annotationClass;
	}

	public Field getField() {
		return f;
	}

	public void setF(Field f) {
		this.f = f;
	}

	public T getAnnot() {
		return annot;
	}

	public void setAnnot(T annot) {
		this.annot = annot;
	}
	
	public Object instance()throws Exception{
		return f.getType().newInstance();
	}
	
	public Object instanceEntity()throws Exception{
		return EntityManager.instanceGeneric(f.getType());
	}
	
	public Object getValueField(Object instance)throws Exception{
		return f.get(instance);
	}
	
	protected static class TypeInfo {
	    Type type;
	    Type name;

	    public TypeInfo(Type type, Type name) {
	        this.type = type;
	        this.name = name;
	    }

	}
	
	
	private static TypeInfo getType(Class<?> clazz, Field field) {
	    TypeInfo type = new TypeInfo(null, null);
	    if (field.getGenericType() instanceof TypeVariable<?>) {
	        TypeVariable<?> genericTyp = (TypeVariable<?>) field.getGenericType();
	        Class<?> superClazz = clazz.getSuperclass();

	        if (clazz.getGenericSuperclass() instanceof ParameterizedType) {
	            ParameterizedType paramType = (ParameterizedType) clazz.getGenericSuperclass();
	            TypeVariable<?>[] superTypeParameters = superClazz.getTypeParameters();
	            if (!Object.class.equals(paramType)) {
	                if (field.getDeclaringClass().equals(superClazz)) {
	                    // this is the root class an starting point for this search
	                    type.name = genericTyp;
	                    type.type = null;
	                } else {
	                    type = getType(superClazz, field);
	                }
	            }
	            if (type.type == null || type.type instanceof TypeVariable<?>) {
	                // lookup if type is not found or type needs a lookup in current concrete class
	                for (int j = 0; j < superClazz.getTypeParameters().length; ++j) {
	                    TypeVariable<?> superTypeParam = superTypeParameters[j];
	                    if (type.name.equals(superTypeParam)) {
	                        type.type = paramType.getActualTypeArguments()[j];
	                        Type[] typeParameters = clazz.getTypeParameters();
	                        if (typeParameters.length > 0) {
	                            for (Type typeParam : typeParameters) {
	                                TypeVariable<?> objectOfComparison = superTypeParam;
	                                if(type.type instanceof TypeVariable<?>) {
	                                    objectOfComparison = (TypeVariable<?>)type.type;
	                                }
	                                if (objectOfComparison.getName().equals(((TypeVariable<?>) typeParam).getName())) {
	                                    type.name = typeParam;
	                                    break;
	                                }
	                            }
	                        }
	                        break;
	                    }
	                }
	            }
	        }
	    } else {
	        type.type = field.getGenericType();
	    }

	    return type;
	}
}
