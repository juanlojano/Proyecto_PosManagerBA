/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.EstadoEntity;
import com.baustro.model.Plazo;
import com.baustro.model.TipoDiferido;
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
public class TipoDiferidoFacade extends AbstractFacade<TipoDiferido> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoDiferidoFacade() {
        super(TipoDiferido.class);
    }

    public List<TipoDiferido> findAllTiposDiferido() {

        return super.findAll();
    }

    public TipoDiferido findTipoDiferidoById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoDiferido> cq = cb.createQuery(TipoDiferido.class);
        Root<TipoDiferido> from = cq.from(TipoDiferido.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<TipoDiferido> q = em.createQuery(cq);
        TipoDiferido tiposDiferido = q.getSingleResult();

        return tiposDiferido;
    }

    public List<TipoDiferido> findAllNoNull() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<TipoDiferido> cq = cb0.createQuery(TipoDiferido.class);
        Root<TipoDiferido> from = cq.from(TipoDiferido.class);
        cq.select(from).where(cb0.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        TypedQuery<TipoDiferido> q = em.createQuery(cq);

        List<TipoDiferido> listaTiposDiferido = q.getResultList();

        return listaTiposDiferido;
    }

}
