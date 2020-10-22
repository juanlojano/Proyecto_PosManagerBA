/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.Autorizacion;
import com.baustro.model.EstadoEntity;
import com.baustro.model.EstadoLote;
import com.baustro.model.Lote;
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
import java.math.BigDecimal;
import javax.inject.Inject;
import org.primefaces.model.SortOrder;

/**
 *
 * @author ue01000632
 */
@Stateless
public class LoteFacade extends AbstractFacade<Lote> {

    private Double valorTransaccionesLote = 0.0;

    @Inject
    private AutorizacionFacade autorizacionFacade;

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoteFacade() {
        super(Lote.class);
    }

    public Lote FindLoteByPinpad(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lote> cq = cb.createQuery(Lote.class);
        Root<Lote> from = cq.from(Lote.class);

        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("pinpad"), id));
        p.add(cb.equal(from.get("estadoLote"), EstadoLote.ABIERTO));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        Lote lote = getEntityManager().createQuery(cq).getSingleResult();

//        
//        
//        
//        cq.select(from).where(cb.equal(from.get("pinpad"), id)).where(cb.equal(from.get("estadoLote"),EstadoLote.ABIERTO));
//        //cb.equal(from.get("estadoPinpad"),EstadoLote.ABIERTO)
//        TypedQuery<Lote> q = em.createQuery(cq);
//        Lote lote = q.getSingleResult();
        /*for(Lote l : lotes ){
            if(l.getEstadoLote() == EstadoLote.ABIERTO){
                lote = l;
            }else            {
                lote = null;
            }
        }*/
        return lote;
    }

    public Lote findLoteByIdPinpad(Long idPinpad) {
        List<Lote> listaLote = new ArrayList<Lote>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lote> cq = cb.createQuery(Lote.class);
        Root<Lote> from = cq.from(Lote.class);
        cq.select(from).where(cb.equal(from.get("pinpad"), idPinpad));
        TypedQuery<Lote> q = em.createQuery(cq);
        listaLote = q.getResultList();
        if (!listaLote.isEmpty()) {
            return listaLote.get(0);
        } else {
            return null;
        }
    }

    public List<Lote> findAllNoNull() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lote> cq = cb.createQuery(Lote.class);
        Root<Lote> from = cq.from(Lote.class);
        cq.select(from).where(cb.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        TypedQuery<Lote> q = em.createQuery(cq);

        List<Lote> listaLotes = q.getResultList();

        return listaLotes;
    }

    public void updateLote(Lote lote) {
        List<Autorizacion> listaTodasAutorizaciones = autorizacionFacade.findAllAutorizacionesByIdLote(lote.getId());
        lote.setNumTotalTrans(listaTodasAutorizaciones.size());
        List<Autorizacion> listaAutorizacionesOk = autorizacionFacade.findAllAutorizacionesOkByIdLote(lote.getId());
        lote.setNumTransOk(listaAutorizacionesOk.size());
        List<Autorizacion> listaAutorizacionesInconsistentes = autorizacionFacade.findAllAutorizacionesInconsistencesByIdLote(lote.getId());
        lote.setNumTransInconsistentes(listaAutorizacionesInconsistentes.size());
        valorTransaccionesLote = 0.0;
        for (Autorizacion aut : listaTodasAutorizaciones) {
            if (aut.getTotalAutoriza() != null) {
                valorTransaccionesLote = valorTransaccionesLote + aut.getTotalAutoriza().doubleValue();
            }
        }
        lote.setValorTotalTrans(valorTransaccionesLote);
        edit(lote);
    }

    public int getLoteTotalCount() {
        Number result = (Number) this.em.createNativeQuery("Select count(e.id) From Lote e").getSingleResult();

        return result.intValue();
    }
    
    public List<Lote> getLoteList(int start, int size,
            String sortField, SortOrder sortOrder) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lote> q = cb.createQuery(Lote.class);
        Root<Lote> r = q.from(Lote.class);
        CriteriaQuery<Lote> select = q.select(r);
        if (sortField != null) {
            q.orderBy(sortOrder == SortOrder.DESCENDING
                    ? cb.asc(r.get(sortField)) : cb.desc(r.get(sortField)));
        }

        TypedQuery<Lote> query = em.createQuery(select);
        query.setFirstResult(start);
        query.setMaxResults(size);
        List<Lote> list = query.getResultList();
        
        return list;
    }

}
