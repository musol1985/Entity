/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.core.injectors;

import java.util.logging.Logger;

import com.entity.core.IEntity;
import com.entity.core.Injector;

/**
 *
 * @author Edu
 */
public abstract class BaseInjector<T extends IEntity> implements Injector<T>,Comparable<BaseInjector>{
	
	protected static final Logger log = Logger.getLogger(BaseInjector.class.getName());
   
    @Override
    public int compareTo(BaseInjector t) {
        return -1;
    }
}
