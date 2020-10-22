/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.interceptors;

import com.baustro.interceptorbinding.LoggingInterceptorBinding;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author ue01000632
 */
@LoggingInterceptorBinding
@Interceptor
public class LoggingInterceptor {
    
    private static final Logger LOGGER = Logger.getLogger(
            LoggingInterceptor.class.getName());

//    @AroundInvoke
//    public Object logMethodCall(InvocationContext invocationContext)
//            throws Exception {
//        LOGGER.log(Level.INFO, new StringBuilder("Empieza metodo: ").append(
//                invocationContext.getMethod().getName()).append(
//                " de la clase: ").append(
//                        invocationContext.getTarget().getClass().getName()).
//                toString());
//        Object retVal = invocationContext.proceed();
//        LOGGER.log(Level.INFO, new StringBuilder("Termina metodo: ").append(
//                invocationContext.getMethod().getName()).append(
//                " de la clase: ").append(
//                        invocationContext.getTarget().getClass().getName()).
//                toString());
//        return retVal;
//    }

    
}
