/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.service;

import com.baustro.interceptorbinding.LoggingInterceptorBinding;
import com.baustro.model.BaseConsumo;
import com.baustro.model.CatalogoError;
import com.baustro.model.Factura;
import com.baustro.model.FacturaAuxiliar;
import com.baustro.sessionbean.BaseConsumoFacade;
import com.baustro.sessionbean.FacturaFacade;
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
import com.baustro.model.Autorizacion;
import com.baustro.model.EstadoEntity;
import com.baustro.model.RespuestaEnum;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.utility.ConverterUtilities;
import java.util.Date;

/**
 *
 * @author ue01000632
 */
@Stateless
@Path("factura")
@LoggingInterceptorBinding
public class FacturaFacadeREST extends AbstractFacade<Factura> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private FacturaFacade facturaFacade;
    @Inject
    private CatalogoError catalogoError;
    @Inject
    private BaseConsumoFacade baseConsumoFacade;
    @Inject
    private AutorizacionFacade autorizacionFacade;
    @Inject
    private ConverterUtilities converter;

    public FacturaFacadeREST() {
        super(Factura.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("crear")
    public Response postCrear(FacturaAuxiliar entity) {
        try {
            Factura factura = new Factura();
            factura.setCodFactura(entity.getCodFactura());
            factura.setFecha(entity.getFecha());
            factura.setIva(entity.getIva());
            factura.setSubTotal(entity.getSubTotal());
            factura.setTotal(entity.getTotal());
            super.create(factura);
            for (BaseConsumo consumo : entity.getConsumos()) {
                consumo.setFactura(facturaFacade.FindFacturaByCodFactura(entity.getCodFactura()));
                baseConsumoFacade.create(consumo);
            }
            for (Autorizacion auth : entity.getAutorizaciones()) {
                Autorizacion autorizacion = autorizacionFacade.FindAutorizacionByCodAutorizacion(auth.getCodAutorizacion());
                autorizacion.setFactura(factura);
                autorizacion.setFechaActualizacion(new Date());
                autorizacion.setEstado(EstadoEntity.MODIFICADA);
                autorizacionFacade.create(autorizacion);
            }            
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.OK.getCodigoRespuesta(), RespuestaEnum.OK.getDescripcionRespuesta(), "Factura Creada"), MediaType.APPLICATION_JSON).build();            
        } catch (Exception e) {
            //catalogoError = returnCustomError(e);
            
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), e.toString()), MediaType.APPLICATION_JSON).build();
        }
    }
//
//    @PUT
//    @Path("{id}")
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public void edit(@PathParam("id") Long id, Factura entity) {
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
//    public Factura find(@PathParam("id") Long id) {
//        return super.find(id);
//    }
////
////    @GET
////    @Override
////    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
////    public List<Factura> findAll() {
////        return super.findAll();
////    }
//
//    @GET
//    @Path("listar")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public Response getAllFacturas() {
//        System.out.println("entró");
//        try {
//            return Response.ok(super.findAll(), MediaType.APPLICATION_JSON).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//    }
//
////    @GET
////    @Path("{from}/{to}")
////    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
////    public List<Factura> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
////        return super.findRange(new int[]{from, to});
////    }
//    @GET
//    @Path("listar/{factura}")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public Response getFacturaByCodigoFactura(@PathParam("factura") String codFactura) {
//        try {
//            System.out.println("entró" + codFactura);
//            return Response.ok(facturaFacade.FindFacturasByCodFactura(codFactura), MediaType.APPLICATION_JSON).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
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
