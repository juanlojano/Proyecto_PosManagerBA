/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.BaseConsumo;
import java.util.List;
import javax.ejb.Stateless;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import com.baustro.service.AbstractFacade;

/**
 *
 * @author ue01000632
 */
@Stateless
public class BaseConsumoFacade extends AbstractFacade<BaseConsumo> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BaseConsumoFacade() {
        super(BaseConsumo.class);
    }

    public List<BaseConsumo> FindBasesConsumos(Long id) {    
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BaseConsumo> cq = cb.createQuery(BaseConsumo.class);
        Root<BaseConsumo> from = cq.from(BaseConsumo.class);
        cq.select(from).where(cb.equal(from.get("factura"),id));
        TypedQuery<BaseConsumo> q = em.createQuery(cq);
        List<BaseConsumo> allitems = q.getResultList();
        return allitems;
    }

}
