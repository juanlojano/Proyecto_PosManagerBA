/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.Comercio;
import com.baustro.model.Voucher;
import com.baustro.service.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ue01000632
 */
@Stateless
public class VoucherFacade extends AbstractFacade<Voucher> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VoucherFacade() {
        super(Voucher.class);
    }    
    
    public Voucher findVoucherByIdAutorizacion(Long id) {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Voucher> cq = cb0.createQuery(Voucher.class);
        Root<Voucher> from = cq.from(Voucher.class);
        cq.select(from).where(cb0.equal(from.get("autorizacion"), id));
        TypedQuery<Voucher> q = em.createQuery(cq);
        
        Voucher voucher = q.getSingleResult();
        
        return voucher;
    }
    
    public Voucher findVoucherBySecuencial(String secuencial) {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Voucher> cq = cb0.createQuery(Voucher.class);
        Root<Voucher> from = cq.from(Voucher.class);
        cq.select(from).where(cb0.equal(from.get("secuencial"), secuencial));
        TypedQuery<Voucher> q = em.createQuery(cq);        
        Voucher voucher = q.getSingleResult();
        
        return voucher;
    }
    
    
}

