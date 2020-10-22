/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.service;

import com.baustro.classes.CustomException;
import com.baustro.interceptorbinding.LoggingInterceptorBinding;
import com.baustro.model.Comercio;
import com.baustro.model.EstadoEntity;
import com.baustro.model.RespuestaEnum;
import com.baustro.utility.ConverterUtilities;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author ue01000632
 */
@Stateless
@Path("comercio")
@LoggingInterceptorBinding
public class ComercioFacadeREST extends AbstractFacade<Comercio> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @Inject
    private ConverterUtilities converter;
    
    private CustomException customException = new CustomException();

    public ComercioFacadeREST() {
        super(Comercio.class);
        System.out.println("sdfsdf");
    }

//    @POST
//    @Override
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public void create(Comercio entity) {
//        super.create(entity);
//    }
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("crear")
    public Response crearComercio(Comercio entity) {
        try {
            entity.setEstado(EstadoEntity.CREADA);
            super.create(entity);
return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.FAIL.getCodigoRespuesta(), RespuestaEnum.FAIL.getDescripcionRespuesta(), entity ), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), e.toString()), MediaType.APPLICATION_JSON).build();
        }
    }
//
//    @PUT
//    @Path("{id}")
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public void edit(@PathParam("id") Long id, Comercio entity) {
//        super.edit(entity);
//    }
//
//    @DELETE
//    @Path("{id}")
//    public void remove(@PathParam("id") Long id) {
//        super.remove(super.find(id));
//    }
//
//    @GET
//    @Path("{id}")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public Comercio find(@PathParam("id") Long id) {
//        return super.find(id);
//    }
//
//    @GET
//    @Override
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public List<Comercio> findAll() {
//        return super.findAll();
//    }
//
//    @GET
//    @Path("{from}/{to}")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public List<Comercio> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
//        return super.findRange(new int[]{from, to});
//    }
//
//    @GET
//    @Path("count")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String countREST() {
//        return String.valueOf(super.count());
//    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
