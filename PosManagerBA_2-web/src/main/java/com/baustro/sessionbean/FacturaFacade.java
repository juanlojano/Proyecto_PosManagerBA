/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import com.baustro.model.Autorizacion;
import com.baustro.model.EstadoAutorizacion;
import com.baustro.model.Factura;
import com.baustro.model.Voucher;
import java.security.acl.Owner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import com.baustro.service.AbstractFacade;
import org.primefaces.model.SortOrder;

/**
 *
 * @author ba0100063v
 */
@Stateless
public class FacturaFacade extends AbstractFacade<Factura> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturaFacade() {
        super(Factura.class);
    }

    public Factura FindFacturaByCodFactura(String codFactura) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factura> cq = cb.createQuery(Factura.class);
        Root<Factura> from = cq.from(Factura.class);
        cq.select(from).where(cb.equal(from.get("codFactura"), codFactura));
        TypedQuery<Factura> q = em.createQuery(cq);
        Factura factura = q.getSingleResult();
        return factura;
    }

    public List<Factura> FindFacturasByCodFactura(String codFactura) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factura> cq = cb.createQuery(Factura.class);
        Root<Factura> from = cq.from(Factura.class);
        cq.select(from).where(cb.equal(from.get("codFactura"), codFactura));
        TypedQuery<Factura> q = em.createQuery(cq);
        List<Factura> facturas = q.getResultList();
        System.out.println("faturas " + facturas);
        return facturas;
    }

    public Factura FindFacturaById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factura> cq = cb.createQuery(Factura.class);
        Root<Factura> from = cq.from(Factura.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<Factura> q = em.createQuery(cq);
        Factura factura = q.getSingleResult();
        return factura;
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

    public Autorizacion findAutorizacionById(Long idAutorizacion) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        cq.select(from).where(cb.equal(from.get("id"), idAutorizacion));
        TypedQuery<Autorizacion> q = em.createQuery(cq);
        Autorizacion autorizacion = q.getSingleResult();

        return autorizacion;
    }

    public List<Factura> findFacturasByRangeDates0(Date fechaInicio, Date fechaFin) {
        List<Factura> listaFacturas = new ArrayList<Factura>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factura> cq = cb.createQuery(Factura.class);

        Root<Factura> from = cq.from(Factura.class);

        List<Predicate> p = new ArrayList<Predicate>();
        //p.add(cb.greaterThan(from.get("fecha"), fechaFin));
        //p.add(cb.lessThan(from.get("fecha"), fechaInicio));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }

        listaFacturas = getEntityManager().createQuery(cq).getResultList();

        return listaFacturas;
    }

//        Root<Autorizacion> from = cq.from(Autorizacion.class);
//
//        List<Predicate> p = new ArrayList<Predicate>();
//        p.add(cb.notEqual(from.get("codAutorizacion"), "null"));
//        p.add(cb.greaterThan(from.get("factura.fecha"), fechaFin));
//        p.add(cb.lessThan(from.get("factura.fecha"), fechaInicio));
    public List<Factura> findFacturasByRangeDates(Date fechaInicio, Date fechaFin) {
        SimpleDateFormat date_f_inicio = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha_Inicio = date_f_inicio.format(fechaInicio);
        SimpleDateFormat date_f_fin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha_Fin = date_f_fin.format(fechaFin);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factura> query = cb.createQuery(Factura.class);
        Root<Factura> from = query.from(Factura.class);
//        cb.between(from.get("fecha"), from.get("fecha"), from.get("fecha"));
//        cb.between(root.get("fecha"), fecha_Inicio, root.get("fecha"));

        List<Factura> listaFacturas = em.createQuery(query).getResultList();
        return listaFacturas;
    }

    public int getFacturaTotalCount() {
        Number result = (Number) this.em.createNativeQuery("Select count(f.id) From Factura f").getSingleResult();

        return result.intValue();
    }

    public List<Factura> getFacturaList(int start, int size,
            String sortField, SortOrder sortOrder) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factura> q = cb.createQuery(Factura.class);
        Root<Factura> r = q.from(Factura.class);
        CriteriaQuery<Factura> select = q.select(r);
        if (sortField != null) {
            q.orderBy(sortOrder == SortOrder.DESCENDING
                    ? cb.asc(r.get(sortField)) : cb.desc(r.get(sortField)));
        }

        TypedQuery<Factura> query = em.createQuery(select);
        query.setFirstResult(start);
        query.setMaxResults(size);
        List<Factura> list = query.getResultList();

        return list;
    }

}
