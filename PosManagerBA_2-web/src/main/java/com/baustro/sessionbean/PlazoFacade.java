/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.EstadoEntity;
import com.baustro.model.Plazo;
import java.util.List;
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
 * @author ue01000632
 */
@Stateless
public class PlazoFacade extends AbstractFacade<Plazo> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlazoFacade() {
        super(Plazo.class);
    }

    public List<Plazo> findAllPlazos() {

        return super.findAll();
    }

    public Plazo findPlazoById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Plazo> cq = cb.createQuery(Plazo.class);
        Root<Plazo> from = cq.from(Plazo.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<Plazo> q = em.createQuery(cq);
        Plazo plazo = q.getSingleResult();
        
        return plazo;
    }

    public List<Plazo> findAllNoNull() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Plazo> cq = cb0.createQuery(Plazo.class);
        Root<Plazo> from = cq.from(Plazo.class);
        cq.select(from).where(cb0.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        TypedQuery<Plazo> q = em.createQuery(cq);

        List<Plazo> listaPlazos = q.getResultList();

        return listaPlazos;
    }

}
