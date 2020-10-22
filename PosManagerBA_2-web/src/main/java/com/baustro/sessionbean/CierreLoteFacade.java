/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.TerminalPinPad;
import com.baustro.model.CierreLote;
import com.baustro.model.EstadoCierre;
import com.baustro.model.EstadoEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.baustro.service.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author ue01000632
 */
@Stateless
public class CierreLoteFacade extends AbstractFacade<CierreLote> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CierreLoteFacade() {
        super(CierreLote.class);
    }

    public CierreLote findByTidPinpad(String tid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CierreLote> cq = cb.createQuery(CierreLote.class);
        Root<CierreLote> from = cq.from(CierreLote.class);
        cq.select(from).where(cb.equal(from.get("tid"), tid));
        TypedQuery<CierreLote> q = em.createQuery(cq);
        CierreLote pinpad = q.getSingleResult();

        return pinpad;
    }

    public int findMaxGroupPinpad() {
//        CriteriaBuilder cb0 = em.getCriteriaBuilder();
//        CriteriaQuery<CierreLote> cq = cb0.createQuery(CierreLote.class);
//        Root<CierreLote> from = cq.from(CierreLote.class);
//        cq.select(from).where(cb0.notEqual(from.get("id"), null));
//        TypedQuery<CierreLote> q = em.createQuery(cq);
//
        List<CierreLote> listaCierre = findAll();

        if (listaCierre.isEmpty()) {
            return 0;
        } else {
            return listaCierre.get(listaCierre.size() - 1).getGrupo();
        }
    }

    public List<CierreLote> findAllLastGroup(int numGrupo) {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<CierreLote> cq = cb0.createQuery(CierreLote.class);
        Root<CierreLote> from = cq.from(CierreLote.class);
        cq.select(from).where(cb0.equal(from.get("grupo"), numGrupo));
        TypedQuery<CierreLote> q = em.createQuery(cq);

        List<CierreLote> listaCierre = q.getResultList();
        if (listaCierre.isEmpty()) {
            return null;
        } else {
            return listaCierre;
        }

    }

    public Boolean findCierreByIdGrupoAndEstado(int numGrupo) {
        List<CierreLote> listaCierre = new ArrayList<CierreLote>();
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<CierreLote> cq = cb0.createQuery(CierreLote.class);

        Root<CierreLote> from = cq.from(CierreLote.class);

//        cb0.and(cb0.notEqual(from.get("estado"), EstadoCierre.CERRADO),
//                cb0.notEqual(from.get("estado"), EstadoCierre.NO_CERRADO),
//                cb0.equal(from.get("grupo"), numGrupo));
//        TypedQuery<CierreLote> tq = getEntityManager().createQuery(cq);
//        listaCierre = tq.getResultList();
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb0.equal(from.get("grupo"), numGrupo));
        p.add(cb0.notEqual(from.get("estado"), EstadoCierre.CERRADO));
        p.add(cb0.notEqual(from.get("estado"), EstadoCierre.NO_CERRADO));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaCierre = getEntityManager().createQuery(cq).getResultList();

        if (!listaCierre.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public CierreLote findByTidPinpadAndGrupo(String tid, int numGrupo) {
        CierreLote cierreLote = new CierreLote();
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<CierreLote> cq = cb0.createQuery(CierreLote.class);

        Root<CierreLote> from = cq.from(CierreLote.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb0.equal(from.get("tid"), tid));
        p.add(cb0.equal(from.get("grupo"), numGrupo));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        cierreLote = getEntityManager().createQuery(cq).getSingleResult();

        return cierreLote;
    }

    public List<CierreLote> findAllLastGroupNotComplete(int numGrupo) {
        List<CierreLote> listaCierre = new ArrayList<CierreLote>();
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<CierreLote> cq = cb0.createQuery(CierreLote.class);

        Root<CierreLote> from = cq.from(CierreLote.class);

        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb0.equal(from.get("grupo"), numGrupo));
        p.add(cb0.notEqual(from.get("estado"), EstadoCierre.CERRADO));
        p.add(cb0.notEqual(from.get("estado"), EstadoCierre.NO_CERRADO));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaCierre = getEntityManager().createQuery(cq).getResultList();

        if (listaCierre.isEmpty()) {
            return null;
        } else {
            return listaCierre;
        }
    }
}
