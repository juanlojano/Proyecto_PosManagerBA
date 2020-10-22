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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.baustro.service.AbstractFacade;


/**
 *
 * @author ba0100063v
 */
@Stateless
public class ErrorFacade extends AbstractFacade<Error> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public ErrorFacade(Class<Error> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ErrorFacade() {
        super(Error.class);
    }

    public CatalogoError FindErrorByCodigo(String codError) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CatalogoError> cq = cb.createQuery(CatalogoError.class);
        Root<CatalogoError> from = cq.from(CatalogoError.class);
        cq.select(from).where(cb.equal(from.get("codError"), codError));
        TypedQuery<CatalogoError> q = em.createQuery(cq);
        CatalogoError catalogoError = q.getSingleResult();
        if (catalogoError != null) {
            return catalogoError;
        }
        return null;
    }

}
