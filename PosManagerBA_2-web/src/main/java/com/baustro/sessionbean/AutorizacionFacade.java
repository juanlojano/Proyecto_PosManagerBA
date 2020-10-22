/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.sessionbean;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import javax.ejb.Stateless;
import com.baustro.model.Autorizacion;
import com.baustro.model.EstadoAutorizacion;
import com.baustro.model.Parametros;
import com.baustro.model.TipoParametro;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Predicate;
import com.baustro.service.AbstractFacade;
import javax.ejb.EJB;
import org.primefaces.model.SortOrder;

/**
 *
 * @author ue01000632
 */
@Stateless
//@EJB
public class AutorizacionFacade extends AbstractFacade<Autorizacion> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AutorizacionFacade() {
        super(Autorizacion.class);
    }

    public List<Autorizacion> findAllNoNull() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        cq.select(from).where(cb0.notEqual(from.get("codAutorizacion"), "null"));
        TypedQuery<Autorizacion> q = em.createQuery(cq);

        List<Autorizacion> listaAutorizacion = q.getResultList();

        return listaAutorizacion;
    }

    public List<Autorizacion> findAllAutorizacionesByIdFactura(Long id) {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        cq.select(from).where(cb0.equal(from.get("factura"), id));
        TypedQuery<Autorizacion> q = em.createQuery(cq);

        List<Autorizacion> listaAutorizacion = q.getResultList();

        return listaAutorizacion;
    }

    public Autorizacion findAutorizacionByIdFactura(Long id) {
        List<Autorizacion> listaAutorizacion = new ArrayList<Autorizacion>();
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        cq.select(from).where(cb0.equal(from.get("factura"), id));
        TypedQuery<Autorizacion> q = em.createQuery(cq);

//        Autorizacion autorizacion = q.getSingleResult();
        listaAutorizacion = q.getResultList();

        if (!listaAutorizacion.isEmpty()) {
            return listaAutorizacion.get(0);
        } else {
            return null;
        }
    }

    public Autorizacion findLastAutorizacionByIdFactura(Long id) {
        List<Autorizacion> listaAutorizacion = new ArrayList<Autorizacion>();
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        cq.select(from).where(cb0.equal(from.get("factura"), id));
        TypedQuery<Autorizacion> q = em.createQuery(cq);

        listaAutorizacion = q.getResultList();

        if (!listaAutorizacion.isEmpty()) {
            return listaAutorizacion.get(listaAutorizacion.size() - 1);
        } else {
            return null;
        }
    }

    public Autorizacion findLastAutorizacion() {
        List<Autorizacion> lista = findAll();
        if (!lista.isEmpty() && lista.size() == 1) {
            lista.get(0).setCodAutorizacion("000001");
            return lista.get(0);
        }

        if (!lista.isEmpty() && lista.size() == 2) {
            lista.get(0).setCodAutorizacion("000002");
            return lista.get(0);
        }

        if (lista.size() > 2) {
//            List<Autorizacion> listaAutorizacion = new ArrayList<Autorizacion>();
//            CriteriaBuilder cb0 = em.getCriteriaBuilder();
//            CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);
//            Root<Autorizacion> from = cq.from(Autorizacion.class);
//            cq.select(from).where(cb0.notEqual(from.get("codAutorizacion"), null));
//            TypedQuery<Autorizacion> q = em.createQuery(cq);
//
//            listaAutorizacion = q.getResultList();

//            if (!listaAutorizacion.isEmpty()) {
            return lista.get(lista.size() - 2);
//            } else {
//                return null;
//            }
        } else {
            return null;
        }
//        
//        
//        List<Autorizacion> listaAutorizacion = new ArrayList<Autorizacion>();
//        CriteriaBuilder cb0 = em.getCriteriaBuilder();
//        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);
//        Root<Autorizacion> from = cq.from(Autorizacion.class);
//        cq.select(from).where(cb0.notEqual(from.get("codAutorizacion"), null));
//        TypedQuery<Autorizacion> q = em.createQuery(cq);
//
//        listaAutorizacion = q.getResultList();
//
//        if (!listaAutorizacion.isEmpty()) {
//            return listaAutorizacion.get(listaAutorizacion.size() - 1);
//        } else {
//            return null;
//        }
    }

    public Autorizacion findLastAutorizacionByIdFacturaAndEstado(Long id) {
        List<Autorizacion> listaAutorizacion = new ArrayList<Autorizacion>();
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);

        Root<Autorizacion> from = cq.from(Autorizacion.class);

        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb0.equal(from.get("factura"), id));
        p.add(cb0.equal(from.get("estadoAutorizacion"), EstadoAutorizacion.NORMAL));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }

        listaAutorizacion = getEntityManager().createQuery(cq).getResultList();

        if (!listaAutorizacion.isEmpty()) {
            return listaAutorizacion.get(listaAutorizacion.size() - 1);
        } else {
            return null;
        }
    }

    public Boolean existAutorizacionByIdFactura(Long id) {
        List<Autorizacion> listaAutorizacion = new ArrayList<Autorizacion>();
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        cq.select(from).where(cb0.equal(from.get("factura"), id));
        TypedQuery<Autorizacion> q = em.createQuery(cq);

        listaAutorizacion = q.getResultList();

        if (!listaAutorizacion.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Autorizacion FindAutorizacionById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<Autorizacion> q = em.createQuery(cq);
        Autorizacion autorizacion = q.getSingleResult();
        return autorizacion;
    }

    public Autorizacion FindAutorizacionByCodAutorizacion(String codAutorizacion) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("codAutorizacion"), codAutorizacion));
//        p.add(cb.equal(from.get("estadoAutorizacion"), EstadoAutorizacion.NORMAL));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }

//        TypedQuery<Autorizacion> q = em.createQuery(cq);
        Autorizacion autorizacion = getEntityManager().createQuery(cq).getSingleResult();
        return autorizacion;
    }

//    public List<Autorizacion> findAllItemsForConciliacion() {
//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Autorizacion.class);
//        Root autorizacion = criteriaQuery.from(Autorizacion.class);
//
//        // Join-Methode wird ein JoinType zur Steuerung übergeben
////        SetJoin adresses = factura.join(Factura_.adresses, JoinType.LEFT);
////        criteriaQuery
////                .select(factura)
////                .where(
////                        criteriaBuilder.equal(adresses.get(Address_.street), "Weinheimer str.")
////                );
////        criteriaQuery.select(factura);
//        List resultList = em.createQuery(criteriaQuery).getResultList();
//        return null;
//    }
//    public List<Autorizacion> findAutorizacionesByRangeDatesJOIN(Date fechaInicio, Date fechaFin) {
//        List<Autorizacion> listaAutorizacionesFiltradas = new ArrayList<Autorizacion>();
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//
//        Metamodel m = em.getMetamodel();
//        EntityType<Autorizacion> Auth_ = m.entity(Autorizacion.class);
//        EntityType<Factura> Fac_ = m.entity(Factura.class);
//        
//        CriteriaQuery<Autorizacion> cq = cb.createQuery(Autorizacion.class);
//        Root<Autorizacion> autho=cq.from(Autorizacion.class);
//        SetJoin<autho, Factura> facturaAutorizacion=autho.join(Auth_.);
//        
//        Root<Autorizacion> auth = cq.from(Autorizacion.class);
//        
//        Join<Factura, Autorizacion> joinAuthFac = cq.join(Auth_.owners).join(Fac_.addresses);
//
//
//        Root<Autorizacion> from = cq.from(Autorizacion.class);
//
//        List<Predicate> p = new ArrayList<Predicate>();
//        p.add(cb.greaterThan(from.get("factura.fecha"), fechaFin));
//        p.add(cb.lessThan(from.get("factura.fecha"), fechaInicio));
//
//        if (!p.isEmpty()) {
//            Predicate[] pr = new Predicate[p.size()];
//            p.toArray(pr);
//            cq.where(pr);
//        }
//
//        listaAutorizacionesFiltradas = getEntityManager().createQuery(cq).getResultList();
//
//        return listaAutorizacionesFiltradas;
//    }
    public List<Autorizacion> findAutorizacionesByRangeDates(Date fechaInicio, Date fechaFin) {
//        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Autorizacion> listaAutorizacionesFiltradas = new ArrayList<Autorizacion>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb.createQuery(Autorizacion.class);

        Root<Autorizacion> from = cq.from(Autorizacion.class);

        Predicate date = cb.between(from.get("factura").get("fecha"), fechaInicio, fechaFin);

        List<Predicate> p = new ArrayList<Predicate>();
//        p.add(cb.notEqual(from.get("codAutorizacion"), "null"));
//        p.add(cb.greaterThan((from.get("factura")).get("fecha"), (fechaFin)));
//        p.add(cb.lessThan((from.get("factura")).get("fecha"), (fechaInicio)));

        p.add(date);

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }

        listaAutorizacionesFiltradas = getEntityManager().createQuery(cq).getResultList();

        return listaAutorizacionesFiltradas;
    }

    public Parametros obtenerDescripcionParametro(Integer plazoDiferido, TipoParametro tipoParametro) {
        List<Parametros> listaParametros = new ArrayList<Parametros>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Parametros> cq = cb.createQuery(Parametros.class);

        Root<Parametros> from = cq.from(Parametros.class);

        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("paramValor1"), String.valueOf(plazoDiferido)));
        p.add(cb.equal(from.get("paramCodigo"), tipoParametro.getValue()));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }

        listaParametros = getEntityManager().createQuery(cq).getResultList();

        if (!listaParametros.isEmpty()) {
            return listaParametros.get(listaParametros.size() - 1);
        } else {
            return null;
        }
    }

    public int getAutorizacionTotalCount() {
        Number result = (Number) this.em.createNativeQuery("Select count(e.id) From Autorizacion e").getSingleResult();

        return result.intValue();
    }

    public List<Autorizacion> getAutorizacionList(int start, int size,
            String sortField, SortOrder sortOrder) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> q = cb.createQuery(Autorizacion.class);
        Root<Autorizacion> r = q.from(Autorizacion.class);
        CriteriaQuery<Autorizacion> select = q.select(r);
        if (sortField != null) {
            q.orderBy(sortOrder == SortOrder.DESCENDING
                    ? cb.asc(r.get(sortField)) : cb.desc(r.get(sortField)));
        }

        TypedQuery<Autorizacion> query = em.createQuery(select);
        query.setFirstResult(start);
        query.setMaxResults(size);
        List<Autorizacion> list = query.getResultList();
        return list;
    }

    public List<Autorizacion> findAllAutorizacionesOkByIdLote(Long idLote) {
        List<Autorizacion> listaAutorizacionesOk = new ArrayList<Autorizacion>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);

        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("idLote"), idLote));
        p.add(cb.equal(from.get("estadoAutorizacion"), 0));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaAutorizacionesOk = getEntityManager().createQuery(cq).getResultList();
        if (listaAutorizacionesOk != null && !listaAutorizacionesOk.isEmpty()) {
            return listaAutorizacionesOk;
        }
        return new ArrayList<Autorizacion>();
    }

    public List<Autorizacion> findAllAutorizacionesInconsistencesByIdLote(Long idLote) {
        List<Autorizacion> listaAutorizacionesInconsistentes = new ArrayList<Autorizacion>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);

        List<Predicate> p = new ArrayList<Predicate>();
        p.add(cb.equal(from.get("idLote"), idLote));
        p.add(cb.notEqual(from.get("estadoAutorizacion"), 0));

        if (!p.isEmpty()) {
            Predicate[] pr = new Predicate[p.size()];
            p.toArray(pr);
            cq.where(pr);
        }
        listaAutorizacionesInconsistentes = getEntityManager().createQuery(cq).getResultList();
        if (listaAutorizacionesInconsistentes != null && !listaAutorizacionesInconsistentes.isEmpty()) {
            return listaAutorizacionesInconsistentes;
        }
        return new ArrayList<Autorizacion>();
    }

    public List<Autorizacion> findAllAutorizacionesByIdLote(Long idLote) {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        cq.select(from).where(cb0.equal(from.get("idLote"), idLote));
        TypedQuery<Autorizacion> q = em.createQuery(cq);

        List<Autorizacion> listaAutorizacion = q.getResultList();

        return listaAutorizacion;
    }

    public Autorizacion findCodAutByIdCaja(Long idTerminalCaja) {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);
        Root<Autorizacion> from = cq.from(Autorizacion.class);
        cq.select(from).where(cb0.equal(from.get("terminalCaja"), idTerminalCaja));
        TypedQuery<Autorizacion> q = em.createQuery(cq);

        List<Autorizacion> listaAutorizacion = q.getResultList();

        if (!listaAutorizacion.isEmpty()) {
            return listaAutorizacion.get(listaAutorizacion.size() - 1);
        } else {
            return null;
        }
    }
}
