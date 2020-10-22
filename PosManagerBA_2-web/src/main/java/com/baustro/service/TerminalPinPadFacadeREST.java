/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.service;

import com.baustro.bean.almacenes.ManejadorColaAlmacenes;
import com.baustro.interceptorbinding.LoggingInterceptorBinding;
import com.baustro.model.EstadoEntity;
import com.baustro.model.RespuestaCierresErrorEnum;
import com.baustro.model.RespuestaEnum;
import com.baustro.model.TerminalPinPad;
import com.baustro.sessionbean.TerminalPinPadFacade;
import com.baustro.utility.ConverterUtilities;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ue01000632
 */
@Stateless
@Path("terminalpinpad")
@LoggingInterceptorBinding
public class TerminalPinPadFacadeREST extends AbstractFacade<TerminalPinPad> {

    @Inject
    private TerminalPinPadFacade terminalPinPadFacade;
    @Inject
    private ManejadorColaAlmacenes colaAlmacenes;
    @Inject
    private ConverterUtilities converter;

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public TerminalPinPadFacadeREST() {
        super(TerminalPinPad.class);
    }

//    @POST
//    @Override
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public void create(TerminalPinPad entity) {
//        super.create(entity);
//    }
    @POST
    @Path("crear")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postCrearPinpad(TerminalPinPad entity) {
        try {
            entity.setEstado(EstadoEntity.CREADA);
            super.create(entity);
            return Response.ok("PinPad Creado", MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("cerrar")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response putCerrarPinpad(TerminalPinPad entity) {
        try {
            if (entity.getIp() != null) {
                entity = terminalPinPadFacade.findTerminalPinPadByIp(entity.getIp());
            }
            if (entity.getTid() != null) {
                entity = terminalPinPadFacade.findTerminalPinPadByTID(entity.getTid());
            }
            String respuestaCierre = colaAlmacenes.cierrePinPad(entity);
            if (respuestaCierre.compareTo("java.net.ConnectException: Connection timed out: connect") == 0) {

                return Response.ok(converter.castToRespuestaCompleta(RespuestaCierresErrorEnum.ERRORTIMEOUT.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), RespuestaCierresErrorEnum.ERRORTIMEOUT.getDescripcionRespuesta()), MediaType.APPLICATION_JSON).build();
            }

            String codRepuesta = respuestaCierre.substring(6, 8);
            Boolean contieneError = respuestaCierre.contains("ERROR");
            if (contieneError == true) {
                if (codRepuesta.compareTo("01") == 0) {

                    return Response.ok(converter.castToRespuestaCompleta(RespuestaCierresErrorEnum.ERRORTRAMA.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), RespuestaCierresErrorEnum.ERRORTRAMA.getDescripcionRespuesta()), MediaType.APPLICATION_JSON).build();
                }
                if (codRepuesta.compareTo("02") == 0) {

                    return Response.ok(converter.castToRespuestaCompleta(RespuestaCierresErrorEnum.ERRORPINPAD.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), RespuestaCierresErrorEnum.ERRORPINPAD.getDescripcionRespuesta()), MediaType.APPLICATION_JSON).build();
                }
                if (codRepuesta.compareTo("06") == 0) {

                    return Response.ok(converter.castToRespuestaCompleta(RespuestaCierresErrorEnum.ERRORLOTEVACIO.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), RespuestaCierresErrorEnum.ERRORLOTEVACIO.getDescripcionRespuesta()), MediaType.APPLICATION_JSON).build();
                }
                if (codRepuesta.compareTo("20") == 0) {

                    return Response.ok(converter.castToRespuestaCompleta(RespuestaCierresErrorEnum.ERRORPROCESO.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), RespuestaCierresErrorEnum.ERRORPROCESO.getDescripcionRespuesta()), MediaType.APPLICATION_JSON).build();
                }
                if (codRepuesta.compareTo("TO") == 0) {

                    return Response.ok(converter.castToRespuestaCompleta(RespuestaCierresErrorEnum.ERRORTIMEOUT.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), RespuestaCierresErrorEnum.ERRORTIMEOUT.getDescripcionRespuesta()), MediaType.APPLICATION_JSON).build();
                }
                if (codRepuesta.compareTo("ER") == 0) {

                    return Response.ok(converter.castToRespuestaCompleta(RespuestaCierresErrorEnum.ERRORCONEXIONPINPAD.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), RespuestaCierresErrorEnum.ERRORCONEXIONPINPAD.getDescripcionRespuesta()), MediaType.APPLICATION_JSON).build();
                }

                return Response.ok(converter.castToRespuestaCompleta(RespuestaCierresErrorEnum.ERRORPINPAD.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), RespuestaCierresErrorEnum.ERRORPINPAD.getDescripcionRespuesta()), MediaType.APPLICATION_JSON).build();
            } else {

                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.OK.getCodigoRespuesta(), RespuestaEnum.OK.getDescripcionRespuesta(), "Ok"), MediaType.APPLICATION_JSON).build();
            }
            //return Response.ok("PinPad Cerrado",MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            //return Response.status(Response.Status.BAD_REQUEST).build();
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), ex.toString()), MediaType.APPLICATION_JSON).build();
        }
    }

    /*
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, TerminalPinPad entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TerminalPinPad find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TerminalPinPad> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TerminalPinPad> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }*/
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
