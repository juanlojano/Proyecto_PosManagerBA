/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.jaas.authentication;

import com.baustro.model.Comercio;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Rol;
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
 * @author ba0100063v
 */
@Stateless
public class RolFacade extends AbstractFacade<Rol> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }

    public List<Rol> findAllRoles() {
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Rol> cq = cb0.createQuery(Rol.class);
        Root<Rol> from = cq.from(Rol.class);
        cq.select(from).where(cb0.notEqual(from.get("id"), null));
        TypedQuery<Rol> q = em.createQuery(cq);

        List<Rol> listaRol = q.getResultList();

        return listaRol;
    }
}
