/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author ue01000632
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {       
        resources.add(com.baustro.service.AutorizacionFacadeREST.class);
        resources.add(com.baustro.service.BaseConsumoFacadeREST.class);
        resources.add(com.baustro.service.ComercioFacadeREST.class);
        resources.add(com.baustro.service.FacturaFacadeREST.class);
        resources.add(com.baustro.service.ImpuestoFacadeREST.class);
        resources.add(com.baustro.service.LoteFacadeREST.class);
        resources.add(com.baustro.service.TerminalCajaFacadeREST.class);
        resources.add(com.baustro.service.TerminalPinPadFacadeREST.class);
        resources.add(com.baustro.utility.CORSFilter.class);
        resources.add(service.CatalogoErrorFacadeREST.class);
    }
    
}