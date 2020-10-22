/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.BinTarjeta;
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
public class BinTarjetaFacade extends AbstractFacade<BinTarjeta> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BinTarjetaFacade() {
        super(BinTarjeta.class);
    }

    public List<BinTarjeta> findAllBinesTarjeta() {

        return super.findAll();
    }

    public BinTarjeta findBinTarjetaById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BinTarjeta> cq = cb.createQuery(BinTarjeta.class);
        Root<BinTarjeta> from = cq.from(BinTarjeta.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<BinTarjeta> q = em.createQuery(cq);
        BinTarjeta binesTarjeta = q.getSingleResult();

        return binesTarjeta;
    }

    public List<BinTarjeta> findAllNoNull() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<BinTarjeta> cq = cb0.createQuery(BinTarjeta.class);
        Root<BinTarjeta> from = cq.from(BinTarjeta.class);
        cq.select(from).where(cb0.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        TypedQuery<BinTarjeta> q = em.createQuery(cq);

        List<BinTarjeta> listaBinesTarjeta = q.getResultList();

        return listaBinesTarjeta;
    }

    public boolean findBinTarjByCodCodTipPag(Long idTipPag) {
        List<BinTarjeta> listaBinTarjeta = new ArrayList<BinTarjeta>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BinTarjeta> cq = cb.createQuery(BinTarjeta.class);
        Root<BinTarjeta> from = cq.from(BinTarjeta.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("tipoPago"), idTipPag));
        p.add(cb.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaBinTarjeta = getEntityManager().createQuery(cq).getResultList();

        if (!listaBinTarjeta.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean findBinTarjetaByBin(String numeroBinTarjeta) {
        List<BinTarjeta> listaBinTarjeta = new ArrayList<BinTarjeta>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BinTarjeta> cq = cb.createQuery(BinTarjeta.class);
        Root<BinTarjeta> from = cq.from(BinTarjeta.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("numero"), numeroBinTarjeta));
        p.add(cb.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaBinTarjeta = getEntityManager().createQuery(cq).getResultList();

        if (!listaBinTarjeta.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
