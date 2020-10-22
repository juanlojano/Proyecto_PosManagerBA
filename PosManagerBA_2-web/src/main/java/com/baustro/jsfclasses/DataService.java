/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.jsfclasses;

import com.baustro.model.Autorizacion;
import com.baustro.service.AbstractFacade;
import org.fluttercode.datafactory.impl.DataFactory;
import org.primefaces.model.SortOrder;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class DataService {
//public enum DataService {
//    INSTANCE {
//        @Override
//        protected EntityManager getEntityManager() {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        }
//    };
//    private final EntityManagerFactory emf =
//            Persistence.createEntityManagerFactory("employee-unit");

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public DataService() {
    }

//    DataService() {
    //persisting some data in database
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        DataFactory dataFactory = new DataFactory();
//        for (int i = 1; i < 100; i++) {
//            Autorizacion employee = new Autorizacion();
//            employee.setName(dataFactory.getName());
//            employee.setPhoneNumber(String.format("%s-%s-%s", dataFactory.getNumberText(3),
//                    dataFactory.getNumberText(3),
//                    dataFactory.getNumberText(4)));
//            employee.setAddress(dataFactory.getAddress() + "," + dataFactory.getCity());
//            em.persist(employee);
//        }
//        em.getTransaction().commit();
//        em.close();
//    }
    public List<Autorizacion> getAutorizacionList(int start, int size,
            String sortField, SortOrder sortOrder) {
//        EntityManager em = emf.createEntityManager();
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

    public int getAutorizacionTotalCount() {
//        EntityManager em = emf.createEntityManager();
//        Query query = emf.createQuery("Select count(e.id) From Autorizacion e");

//        return ((Long) query.getSingleResult()).intValue();
        CriteriaBuilder cb0 = em.getCriteriaBuilder();
        CriteriaQuery<Autorizacion> cq = cb0.createQuery(Autorizacion.class);

        CriteriaQuery<Long> criteriaQuery = cb0.createQuery(Long.class);
        Root<Autorizacion> root = cq.from(Autorizacion.class);
        criteriaQuery.select(cb0.count(root));

        TypedQuery<Long> q = em.createQuery(criteriaQuery);
        long count = q.getSingleResult();

        return (int) count;

//        javax.persistence.criteria.CriteriaQuery cqq = getEntityManager().getCriteriaBuilder().createQuery();
//        javax.persistence.criteria.Root<Autorizacion> rt = cqq.from(Autorizacion.class);
//        cqq.select(getEntityManager().getCriteriaBuilder().count(rt));
//        javax.persistence.Query qq = getEntityManager().createQuery(cqq);
//        
//        return ((Long) qq.getSingleResult()).intValue();        
    }
}
