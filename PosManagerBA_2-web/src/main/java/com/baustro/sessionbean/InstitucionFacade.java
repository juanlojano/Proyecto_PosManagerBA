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
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.baustro.service.AbstractFacade;

/**
 *
 * @author ue01000632
 */
@Stateless
public class InstitucionFacade extends AbstractFacade<Institucion> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InstitucionFacade() {
        super(Institucion.class);
    }

    public List<Institucion> findAllInstituciones() {

        return super.findAll();
    }

    public Institucion findInstitucionById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Institucion> cq = cb.createQuery(Institucion.class);
        Root<Institucion> from = cq.from(Institucion.class);
        cq.select(from).where(cb.equal(from.get("id"), id));
        TypedQuery<Institucion> q = em.createQuery(cq);
        Institucion institucion = q.getSingleResult();

        return institucion;
    }

    public List<Institucion> findAllNoNull() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Institucion> cq = cb0.createQuery(Institucion.class);
        Root<Institucion> from = cq.from(Institucion.class);
        cq.select(from).where(cb0.notEqual(from.get("estado"), EstadoEntity.ELIMINADA));
        TypedQuery<Institucion> q = em.createQuery(cq);

        List<Institucion> listaInstituciones = q.getResultList();

        return listaInstituciones;
    }

}
