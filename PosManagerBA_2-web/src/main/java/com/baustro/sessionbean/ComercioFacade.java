/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.Comercio;
import com.baustro.model.EstadoEntity;
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
import org.primefaces.model.SortOrder;


/**
 *
 * @author ue01000632
 */
@Stateless
public class ComercioFacade extends AbstractFacade<Comercio> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComercioFacade() {
        super(Comercio.class);
    }

    public List<Comercio> findAllComercios() {

        return super.findAll();
    }

    public Comercio findComercioById(Long id) {
        System.out.println("-----------findCom: id " + id);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Comercio> cq = cb.createQuery(Comercio.class);
        Root<Comercio> from = cq.from(Comercio.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<Comercio> q = em.createQuery(cq);
        Comercio comercio = q.getSingleResult();
        System.out.println("-------------comercio: " + comercio);

        return comercio;
    }

    public Comercio findComercioByCodigoComercio(Long id) {
        System.out.println("-----------findCom: id " + id);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Comercio> cq = cb.createQuery(Comercio.class);
        Root<Comercio> from = cq.from(Comercio.class);
        cq.select(from).where(cb.equal(from.get("codComercio"), id.toString()));
        TypedQuery<Comercio> q = em.createQuery(cq);
        Comercio comercio = q.getSingleResult();
        System.out.println("-------------comercio: " + comercio);

        return comercio;
    }

    public Comercio findComercioByCodigoComercioBA(Long id) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Comercio> cq = cb.createQuery(Comercio.class);
//        Root<Comercio> from = cq.from(Comercio.class);
//        cq.select(from).where(cb.equal(from.get("codComercioBa"), id.toString()));
//        TypedQuery<Comercio> q = em.createQuery(cq);
//        Comercio comercio = q.getSingleResult();
//
//        return comercio;
        
        
        
        List<Comercio> listaComercio = new ArrayList<Comercio>();
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Comercio> cq = cb0.createQuery(Comercio.class);
        Root<Comercio> from = cq.from(Comercio.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb0.equal(from.get("codComercioBa"), id.toString()));
        p.add(cb0.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }

        listaComercio = getEntityManager().createQuery(cq).getResultList();

        if (!listaComercio.isEmpty()) {
            return listaComercio.get(listaComercio.size() - 1);
        } else {
            return null;
        }
        
    }

    public List<Comercio> findAllNoNull() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Comercio> cq = cb0.createQuery(Comercio.class);
        Root<Comercio> from = cq.from(Comercio.class);
        cq.select(from).where(cb0.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        TypedQuery<Comercio> q = em.createQuery(cq);

        List<Comercio> listaComercio = q.getResultList();

        return listaComercio;
    }
    
    
    public int getComercioTotalCount() {
        Number result = (Number) this.em.createNativeQuery("Select count(c.id) From Comercio c").getSingleResult();

        return result.intValue();
    }

    public List<Comercio> getComercioList(int start, int size,
            String sortField, SortOrder sortOrder) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Comercio> q = cb.createQuery(Comercio.class);
        Root<Comercio> r = q.from(Comercio.class);
        CriteriaQuery<Comercio> select = q.select(r);
        if (sortField != null) {
            q.orderBy(sortOrder == SortOrder.DESCENDING
                    ? cb.asc(r.get(sortField)) : cb.desc(r.get(sortField)));
        }

        TypedQuery<Comercio> query = em.createQuery(select);
        query.setFirstResult(start);
        query.setMaxResults(size);
        List<Comercio> list = query.getResultList();
        return list;
    }
    

}
