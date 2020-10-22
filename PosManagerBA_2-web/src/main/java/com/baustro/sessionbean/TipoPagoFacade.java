/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.EstadoEntity;
import com.baustro.model.Institucion;
import com.baustro.model.Plazo;
import com.baustro.model.TipoDiferido;
import com.baustro.model.TipoPago;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.baustro.service.AbstractFacade;
import java.util.ArrayList;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author ue01000632
 */
@Stateless
public class TipoPagoFacade extends AbstractFacade<TipoPago> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoPagoFacade() {
        super(TipoPago.class);
    }

    public List<TipoPago> findAllTiposPago() {

        return super.findAll();
    }

    public TipoPago findTipoPagoById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoPago> cq = cb.createQuery(TipoPago.class);
        Root<TipoPago> from = cq.from(TipoPago.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<TipoPago> q = em.createQuery(cq);
        TipoPago tiposPago = q.getSingleResult();

        return tiposPago;
    }

    public List<TipoPago> findAllNoNull() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<TipoPago> cq = cb0.createQuery(TipoPago.class);
        Root<TipoPago> from = cq.from(TipoPago.class);
        cq.select(from).where(cb0.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        TypedQuery<TipoPago> q = em.createQuery(cq);

        List<TipoPago> listaTiposPago = q.getResultList();

        return listaTiposPago;
    }

    public boolean findTipoPagoByCodTipoDiferido(Long tipoDiferido) {
        List<TipoPago> listaTipoPago = new ArrayList<TipoPago>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoPago> cq = cb.createQuery(TipoPago.class);
        Root<TipoPago> from = cq.from(TipoPago.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("tipoDiferido"), tipoDiferido));
        p.add(cb.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaTipoPago = getEntityManager().createQuery(cq).getResultList();

        if (!listaTipoPago.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean findTipoPagoByCodPlazo(Long idPlazo) {
        List<TipoPago> listaTipoPago = new ArrayList<TipoPago>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoPago> cq = cb.createQuery(TipoPago.class);
        Root<TipoPago> from = cq.from(TipoPago.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("plazo"), idPlazo));
        p.add(cb.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaTipoPago = getEntityManager().createQuery(cq).getResultList();

        if (!listaTipoPago.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean findTipoPagoByCodInstitucion(Long idInstitucion) {
        List<TipoPago> listaTipoPago = new ArrayList<TipoPago>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TipoPago> cq = cb.createQuery(TipoPago.class);
        Root<TipoPago> from = cq.from(TipoPago.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("institucion"), idInstitucion));
        p.add(cb.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaTipoPago = getEntityManager().createQuery(cq).getResultList();

        if (!listaTipoPago.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}
