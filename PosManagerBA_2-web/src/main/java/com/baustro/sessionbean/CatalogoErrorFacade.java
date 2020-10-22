/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.CatalogoError;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.baustro.service.AbstractFacade;

/**
 *
 * @author ba0100063v
 */
@Stateless
public class CatalogoErrorFacade extends AbstractFacade<CatalogoError> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CatalogoErrorFacade() {
        super(CatalogoError.class);
    }
    
}
