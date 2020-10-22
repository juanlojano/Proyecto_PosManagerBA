/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.service;

import com.baustro.interceptorbinding.LoggingInterceptorBinding;
import com.baustro.model.EstadoEntity;
import com.baustro.model.EstadoLote;
import com.baustro.model.Lote;
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

/**
 *
 * @author ue01000632
 */
@Stateless
@Path("lote")
@LoggingInterceptorBinding
public class LoteFacadeREST extends AbstractFacade<Lote> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private ConverterUtilities converter;

    public LoteFacadeREST() {
        super(Lote.class);
    }

    @POST
    @Path("crear")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response postCrear(Lote entity) {
        try {
            entity.setEstado(EstadoEntity.CREADA);
            entity.setEstadoLote(EstadoLote.ABIERTO);
            super.create(entity);
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.OK.getCodigoRespuesta(), RespuestaEnum.OK.getDescripcionRespuesta(), entity), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), e.toString()), MediaType.APPLICATION_JSON).build();
        }
    }
//
//    @PUT
//    @Path("{id}")
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public void edit(@PathParam("id") Long id, Lote entity) {
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
//    public Lote find(@PathParam("id") Long id) {
//        return super.find(id);
//    }
//
//    @GET
//    @Override
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public List<Lote> findAll() {
//        return super.findAll();
//    }
//
//    @GET
//    @Path("{from}/{to}")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public List<Lote> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
