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
import com.baustro.model.Parametros;
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
public class ParametrosFacade extends AbstractFacade<Parametros> {

    private Double valorTransaccionesLote = 0.0;

    @Inject
    private AutorizacionFacade autorizacionFacade;

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParametrosFacade() {
        super(Parametros.class);
    }

    public Parametros findByCodigoRespuesta(String codigoRespuestaAutorizador) {
        
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Parametros> cq = cb0.createQuery(Parametros.class);
        Root<Parametros> from = cq.from(Parametros.class);
        cq.select(from).where(cb0.equal(from.get("paramValor2"), codigoRespuestaAutorizador));
        TypedQuery<Parametros> q = em.createQuery(cq);

        List<Parametros> listaParametros = q.getResultList();

        if (!listaParametros.isEmpty()) {
            return listaParametros.get(listaParametros.size() - 1);
        } else {
            return null;
        }
    }

}
