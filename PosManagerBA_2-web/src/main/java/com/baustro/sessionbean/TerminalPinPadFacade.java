/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.TerminalPinPad;
import com.baustro.model.Comercio;
import com.baustro.model.EstadoEntity;
import com.baustro.model.TerminalPinPad;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.baustro.service.AbstractFacade;
import org.primefaces.model.SortOrder;

/**
 *
 * @author ue01000632
 */
@Stateless
public class TerminalPinPadFacade extends AbstractFacade<TerminalPinPad> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TerminalPinPadFacade() {
        super(TerminalPinPad.class);
    }

    public TerminalPinPad findTerminalPinPadById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TerminalPinPad> cq = cb.createQuery(TerminalPinPad.class);
        Root<TerminalPinPad> from = cq.from(TerminalPinPad.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<TerminalPinPad> q = em.createQuery(cq);
        TerminalPinPad pinpad = q.getSingleResult();
        return pinpad;
    }

    public TerminalPinPad findTerminalPinPadByIp(String ip) {
        TerminalPinPad pinpad = new TerminalPinPad();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TerminalPinPad> cq = cb.createQuery(TerminalPinPad.class);
        Root<TerminalPinPad> from = cq.from(TerminalPinPad.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("ip"), ip));
        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        pinpad = getEntityManager().createQuery(cq).getSingleResult();
        return pinpad;
    }

    public List<TerminalPinPad> findAllNoNull() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<TerminalPinPad> cq = cb0.createQuery(TerminalPinPad.class);
        Root<TerminalPinPad> from = cq.from(TerminalPinPad.class);
        cq.select(from).where(cb0.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        TypedQuery<TerminalPinPad> q = em.createQuery(cq);

        List<TerminalPinPad> listaPinpad = q.getResultList();

        return listaPinpad;
    }

    public TerminalPinPad findTerminalPinPadByTID(String tid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TerminalPinPad> cq = cb.createQuery(TerminalPinPad.class);
        Root<TerminalPinPad> from = cq.from(TerminalPinPad.class);
        cq.select(from).where(cb.equal(from.get("tid"), tid));
        TypedQuery<TerminalPinPad> q = em.createQuery(cq);
        TerminalPinPad pinpad = q.getSingleResult();

        return pinpad;
    }

    public boolean findTerminalPinPadByCodComercio(Long idComercio) {
        List<TerminalPinPad> listaPinpad = new ArrayList<TerminalPinPad>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TerminalPinPad> cq = cb.createQuery(TerminalPinPad.class);
        Root<TerminalPinPad> from = cq.from(TerminalPinPad.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("comercio"), idComercio));
        p.add(cb.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaPinpad = getEntityManager().createQuery(cq).getResultList();

        if (!listaPinpad.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public int getTerminalPinPadTotalCount() {
        Number result = (Number) this.em.createNativeQuery("Select count(tp.id) From TerminalPinPad tp").getSingleResult();

        return result.intValue();
    }

    public List<TerminalPinPad> getTerminalPinPadList(int start, int size,
            String sortField, SortOrder sortOrder) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TerminalPinPad> q = cb.createQuery(TerminalPinPad.class);
        Root<TerminalPinPad> r = q.from(TerminalPinPad.class);
        CriteriaQuery<TerminalPinPad> select = q.select(r);
        if (sortField != null) {
            q.orderBy(sortOrder == SortOrder.DESCENDING
                    ? cb.asc(r.get(sortField)) : cb.desc(r.get(sortField)));
        }

        TypedQuery<TerminalPinPad> query = em.createQuery(select);
        query.setFirstResult(start);
        query.setMaxResults(size);
        List<TerminalPinPad> list = query.getResultList();
        return list;
    }

}
