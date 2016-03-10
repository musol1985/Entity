package com.entity.bean.custom;

import java.lang.reflect.Method;

import com.entity.anot.components.input.Input;
import com.entity.bean.AnnotationMethodBean;

public class InputBean extends AnnotationMethodBean<Input>{
	protected boolean digital;
        protected boolean tpf;
	
	public InputBean(Method m, Class<Input> annotationClass) throws Exception {
		super(m, annotationClass);

		this.digital=isDigitalMethod(m);
                this.tpf=isNeedTpf(m);
	}

	public boolean isDigital() {
		return digital;
	}

		
	private boolean isDigitalMethod(Method m){
		return (m.getParameterTypes().length>0 && m.getParameterTypes()[0]==boolean.class);
	}
	
        private boolean isNeedTpf(Method m){
            return m.getParameterTypes().length==2;
        }
        
        public void invoke(Object obj, Object param1, Object param2)throws Exception{
            m.invoke(obj, param1, param2);
        }
}
