/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity.core.injectors;

import com.entity.core.IEntity;
import com.entity.core.Injector;

/**
 *
 * @author Edu
 */
public abstract class BaseInjector<T extends IEntity> implements Injector<T>,Comparable<BaseInjector>{
   
    @Override
    public int compareTo(BaseInjector t) {
        return -1;
    }
}
