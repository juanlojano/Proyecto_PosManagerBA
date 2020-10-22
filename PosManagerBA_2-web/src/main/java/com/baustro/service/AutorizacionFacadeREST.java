/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.baustro.service;

import com.baustro.bean.almacenes.ManejadorColaAlmacenes;
import com.baustro.classes.VoucherClass;
import com.baustro.interceptorbinding.LoggingInterceptorBinding;
import com.baustro.jsfclasses.VoucherController;
import com.baustro.jsfclasses.util.EncriptaDesencriptarTrama;
import com.baustro.model.Autorizacion;
import com.baustro.model.AutorizacionAuxiliar;
import com.baustro.model.EstadoAutorizacion;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Respuesta;
import com.baustro.model.RespuestaEnum;
import com.baustro.model.TerminalCaja;
import com.baustro.model.TipoTransaccion;
import com.baustro.model.Voucher;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.ComercioFacade;
import com.baustro.sessionbean.LoteFacade;
import com.baustro.sessionbean.TerminalCajaFacade;
import com.baustro.sessionbean.VoucherFacade;
import com.baustro.util.MsgFile;
import com.baustro.utility.ConverterUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("autorizacion")
@LoggingInterceptorBinding
public class AutorizacionFacadeREST extends AbstractFacade<Autorizacion> {

    @PersistenceContext(unitName = "com.baustro_PosManagerBA_2-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private TerminalCajaFacade terminalCajaFacade;
    @Inject
    private VoucherController voucherController;
    @Inject
    private ComercioFacade comercioFacade;
    @Inject
    private Voucher voucher;
    @Inject
    private ConverterUtilities converter;
    @Inject
    private ManejadorColaAlmacenes colaAlmacenes;
    @Inject
    private VoucherFacade voucherFacade;
    @Inject
    private AutorizacionFacade autorizacionFacade;
    @Inject
    private LoteFacade loteFacade;

    public AutorizacionFacadeREST() {
        super(Autorizacion.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("crear")
    public Response postCrear(AutorizacionAuxiliar auxiliar) throws IOException {
        try {
            if ((MsgFile.getText().contains("tramaEnvio") && !MsgFile.getText().contains("respuesta"))
                    || MsgFile.getText().contains("ERROR EN EL SERVIDOR WILDFLY")) {
                Response resp = null;
                resp = validarUltimaTRX();
                resp = procesarTRX(auxiliar);
                return resp;
            }

            if (validarDiferidos(auxiliar.getTipoDiferido(), auxiliar.getPlazoDiferido(), auxiliar.getMesesGracia()) == true) {
                Response resp = null;
                resp = procesarTRX(auxiliar);
                return resp;
            } else {
                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), "ERROR EN DIFERIDOS.  Revisar tipoDiferido, plazoDiferido, mesesGracia"), MediaType.APPLICATION_JSON).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            MsgFile.write("ERROR EN EL SERVIDOR WILDFLY");
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), "ERROR EN EL SERVIDOR WILDFLY"), MediaType.APPLICATION_JSON).build();

        }

    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("anular")
    public Response postAnulacion(AutorizacionAuxiliar auxiliar) {
        try {
            String tramaRespuestaAnulacion = "";

            if (auxiliar.getCodAutorizacion() == null) {
                TerminalCaja terminalCaja = terminalCajaFacade.findTerminalCajaByCodTerminal(auxiliar.getTerminalCaja().getId());
                Autorizacion autorizacion = autorizacionFacade.findCodAutByIdCaja(terminalCaja.getId());
                auxiliar.setCodAutorizacion(autorizacion.getCodAutorizacion());
            }

            Voucher voucherAux = voucherFacade.findVoucherByIdAutorizacion(autorizacionFacade.FindAutorizacionByCodAutorizacion(auxiliar.getCodAutorizacion()).getId());

            String tramaAnulacion = converter.castTramaIngresoToAutorizacion(EncriptaDesencriptarTrama.Desencriptar(voucherAux.getTramaEnvio()), auxiliar.getCodAutorizacion(), voucherAux.getSecuencial());
//            String tramaAnulacion = converter.castTramaIngresoToAutorizacion(voucherAux.getTramaEnvio(), auxiliar.getCodAutorizacion(), voucherAux.getSecuencial());
            auxiliar.setComercio(comercioFacade.findComercioByCodigoComercioBA(auxiliar.getComercio().getId()));
            auxiliar.setTerminalCaja(terminalCajaFacade.findTerminalCajaByCodTerminal(auxiliar.getTerminalCaja().getId()));
            tramaRespuestaAnulacion = colaAlmacenes.EnviarMensajeAnulacion(auxiliar, tramaAnulacion);

            if (tramaRespuestaAnulacion.contains("TRANS CANCELADA")) {

                return Response.ok(converter.castToRespuestaCompleta(converter.obtenerCodigoError(tramaRespuestaAnulacion), converter.obtenerDescripcionError(tramaRespuestaAnulacion), converter.castToVoucher(tramaRespuestaAnulacion)), MediaType.APPLICATION_JSON).build();
            } else if (tramaRespuestaAnulacion.contains("NO ENCONTRADA")) {

                return Response.ok(converter.castToRespuestaCompleta(converter.obtenerCodigoError(tramaRespuestaAnulacion), converter.obtenerDescripcionError(tramaRespuestaAnulacion), converter.castToVoucher(tramaRespuestaAnulacion)), MediaType.APPLICATION_JSON).build();
            } else {
                voucherAux.setTramaAnulacion(tramaAnulacion);
                VoucherClass voucherClass = converter.castToVoucher(tramaRespuestaAnulacion);
                Autorizacion entity = autorizacionFacade.FindAutorizacionByCodAutorizacion(auxiliar.getCodAutorizacion());
                voucherController.create(voucherAux);
                if (voucherClass.getCodigoRespuestaAutorizador().equals("00")) {
                    System.out.println("normal");
                    entity.setCodAutorizacion(voucherClass.getNumeroAutorizacion());
                    entity.setEstadoAutorizacion(EstadoAutorizacion.ANULADA);
                    entity.setFechaActualizacion(new Date());
                    entity.setEstado(EstadoEntity.MODIFICADA);
                    super.create(entity);
                    return Response.ok(converter.castToRespuestaCompleta(converter.obtenerCodigoError(tramaRespuestaAnulacion), converter.obtenerDescripcionError(tramaRespuestaAnulacion), converter.castToVoucher(tramaRespuestaAnulacion)), MediaType.APPLICATION_JSON).build();

                } else {
                    System.out.println("inconsistente");
                    return Response.ok(converter.castToRespuestaCompleta(converter.obtenerCodigoError(voucher.getTramaRespuesta()), converter.obtenerDescripcionError(voucher.getTramaRespuesta()), converter.castToVoucher(voucher.getTramaRespuesta())), MediaType.APPLICATION_JSON).build();
                }
            }
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
            Logger.getLogger(AutorizacionFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), ex.toString()), MediaType.APPLICATION_JSON).build();
        }

    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reverso")
    public Response postReversar(AutorizacionAuxiliar auxiliar) {
        try {
            String tramaRespuestaReverso = "";
            Voucher voucherAux = voucherFacade.findVoucherBySecuencial(auxiliar.getSecuencial());
            String trmReverso = converter.castTramaIngresoToReverso(EncriptaDesencriptarTrama.Desencriptar(voucher.getTramaEnvio()));
            auxiliar.setComercio(comercioFacade.findComercioByCodigoComercioBA(auxiliar.getComercio().getId()));
            auxiliar.setTerminalCaja(terminalCajaFacade.findTerminalCajaByCodTerminal(auxiliar.getTerminalCaja().getId()));
            tramaRespuestaReverso = colaAlmacenes.EnviarMensajeAnulacion(auxiliar, trmReverso);
            voucherAux.setTramaAnulacion(trmReverso);
            Autorizacion entity = autorizacionFacade.FindAutorizacionByCodAutorizacion(auxiliar.getCodAutorizacion());
            voucherController.create(voucherAux);
            VoucherClass voucherClass = converter.castToVoucher(tramaRespuestaReverso);
            if (voucherClass.getCodigoRespuestaAutorizador().equals("00")) {
                entity.setCodAutorizacion(voucherClass.getNumeroAutorizacion());
                entity.setEstadoAutorizacion(EstadoAutorizacion.REVERSO);
                entity.setFechaActualizacion(new Date());
                entity.setEstado(EstadoEntity.MODIFICADA);
                super.create(entity);
                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.OK.getCodigoRespuesta(), RespuestaEnum.OK.getDescripcionRespuesta(), converter.castToVoucher(voucher.getTramaRespuesta())), MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.FAIL.getCodigoRespuesta(), RespuestaEnum.FAIL.getDescripcionRespuesta(), converter.castToVoucher(voucher.getTramaRespuesta())), MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(AutorizacionFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), ex.toString()), MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("testAutorizacion")
    @Produces(value = MediaType.APPLICATION_JSON)
    public List<Autorizacion> findAll() {

        return super.findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Boolean posmanagerAnulacion(Autorizacion autorizacion) {
        try {
            String tramaRespuestaAnulacion = "";
            Voucher voucherAux = voucherFacade.findVoucherByIdAutorizacion(autorizacionFacade.FindAutorizacionByCodAutorizacion(autorizacion.getCodAutorizacion()).getId());
            String tramaAnulacion = converter.castTramaIngresoToAutorizacion(EncriptaDesencriptarTrama.Desencriptar(voucherAux.getTramaEnvio()), autorizacion.getCodAutorizacion(), voucherAux.getSecuencial());
            tramaRespuestaAnulacion = colaAlmacenes.EnviarMensajeAnulacionPosmanager(autorizacion, tramaAnulacion);
            Boolean resultadoNE = tramaRespuestaAnulacion.contains("NO ENCONTRADA");
            Boolean resultadoTO = tramaRespuestaAnulacion.contains("TIMEOUT");
            Boolean resultadoCANCEL = tramaRespuestaAnulacion.contains("TRANS CANCELADA");
            if (resultadoNE == true || resultadoTO == true || resultadoCANCEL == true) {
                return false;
            } else {
                voucherAux.setTramaAnulacion(EncriptaDesencriptarTrama.Encriptar(tramaAnulacion));
                VoucherClass voucherClass = converter.castToVoucher(tramaRespuestaAnulacion);
                voucherController.create(voucherAux);
                if (voucherClass.getCodigoRespuestaAutorizador().equals("00") && tramaRespuestaAnulacion.length() > 250) {
                    System.out.println("normal");
                    autorizacion.setCodAutorizacion(voucherClass.getNumeroAutorizacion());
                    autorizacion.setEstadoAutorizacion(EstadoAutorizacion.ANULADA);
                    autorizacion.setFechaActualizacion(new Date());
                    autorizacion.setEstado(EstadoEntity.MODIFICADA);
                    super.edit(autorizacion);

                    return true;//                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.OK.getCodigoRespuesta(), RespuestaEnum.OK.getDescripcionRespuesta(), converter.castToVoucher(voucher.getTramaRespuesta())), MediaType.APPLICATION_JSON).build();
                } else {
                    System.out.println("inconsistente");

                    return false;
//                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.FAIL.getCodigoRespuesta(), RespuestaEnum.FAIL.getDescripcionRespuesta(), converter.castToVoucher(voucher.getTramaRespuesta())), MediaType.APPLICATION_JSON).build();
                }
            }
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
            Logger.getLogger(AutorizacionFacadeREST.class.getName()).log(Level.SEVERE, null, ex);

            return false;
            //return Response.status(Response.Status.BAD_REQUEST).build();
//            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), ex.toString()), MediaType.APPLICATION_JSON).build();
        }
    }

    private boolean validarDiferidos(Integer tipoDiferido, Integer plazoDiferido, int mesesGracia) {
//        if (tipoDiferido == 0 && plazoDiferido == 0 && mesesGracia == 0) {
//
//            return true;
//        }
//        if (tipoDiferido == 2 && mesesGracia == 0) {
//
//            return true;
//        }
//
//        if (tipoDiferido == 3 && mesesGracia == 0) {
//
//            return true;
//        }
//
//        if (tipoDiferido == 5 && mesesGracia == 2) {
//
//            return true;
//        }
//
//        if (tipoDiferido == 6 && mesesGracia == 3) {
//
//            return true;
//        }
//
//        if (tipoDiferido == 7 && mesesGracia == 1) {
//
//            return true;
//        }
//
//        if (tipoDiferido == 8 && mesesGracia == 2) {
//
//            return true;
//        }
//
//        return false;
        return true;
    }

    private String obtenerCodigoRespuestaTransaccion(String tramaRespuesta) {
        String respuesta = tramaRespuesta.substring(6, 8);

        return respuesta;
    }

    private String obtenerCodigoAdquiriente(String tramaRespuesta) {
        String respuesta = tramaRespuesta.substring(6, 8);

        return respuesta;
    }

    private String obtenerNumeroTarjeta(String tramaRespuesta) {
        String numeroTarjeta = tramaRespuesta.substring(394, 419);

        return numeroTarjeta;
    }

    public Response postReversarTransaccion(AutorizacionAuxiliar auxiliar, Voucher voucher, String tramaEnvio, Autorizacion autorizacion) {
        System.out.println("------------------------------------EN METODO REVERSO");
        try {
            String tramaRespuestaReverso = "";
            Voucher voucherAux = voucher;

            String trmReverso = converter.castTramaIngresoToReverso(tramaEnvio);
            auxiliar.setComercio(comercioFacade.findComercioByCodigoComercioBA(auxiliar.getComercio().getId()));
            auxiliar.setTerminalCaja(terminalCajaFacade.findTerminalCajaByCodTerminal(auxiliar.getTerminalCaja().getId()));
            tramaRespuestaReverso = colaAlmacenes.EnviarMensajeAnulacion(auxiliar, trmReverso);
            voucherAux.setTramaAnulacion(trmReverso);
            Autorizacion entity = autorizacion;
            voucherController.create(voucherAux);
            VoucherClass voucherClass = converter.castToVoucher(tramaRespuestaReverso);
            if (voucherClass.getCodigoRespuestaAutorizador().equals("00")) {
                entity.setCodAutorizacion(voucherClass.getNumeroAutorizacion());
                entity.setEstadoAutorizacion(EstadoAutorizacion.REVERSO);
                entity.setFechaActualizacion(new Date());
                entity.setEstado(EstadoEntity.MODIFICADA);
                super.create(entity);
                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.OK.getCodigoRespuesta(), RespuestaEnum.OK.getDescripcionRespuesta(), converter.castToVoucher(voucher.getTramaRespuesta())), MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.FAIL.getCodigoRespuesta(), RespuestaEnum.FAIL.getDescripcionRespuesta(), converter.castToVoucher(voucher.getTramaRespuesta())), MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(AutorizacionFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), ex.toString()), MediaType.APPLICATION_JSON).build();
        }
    }

    public Response postReversarTransaccion2(AutorizacionAuxiliar auxiliar, Voucher voucher, String tramaEnvio, Autorizacion autorizacion) {
        System.out.println("------------------------------------EN METODO REVERSO");
        try {
            String tramaRespuestaReverso = "";
            Voucher voucherAux = voucher;

            String trmReverso = converter.castTramaIngresoToReverso(tramaEnvio);
            auxiliar.setComercio(comercioFacade.findComercioByCodigoComercioBA(auxiliar.getComercio().getId()));
            auxiliar.setTerminalCaja(terminalCajaFacade.findTerminalCajaByCodTerminal(auxiliar.getTerminalCaja().getId()));
            tramaRespuestaReverso = colaAlmacenes.EnviarMensajeAnulacion(auxiliar, trmReverso);
            voucherAux.setTramaAnulacion(trmReverso);
            Autorizacion entity = converter.castToAutorizacion(auxiliar);
            entity.setComercio(comercioFacade.findComercioByCodigoComercioBA(entity.getComercio().getId()));
            entity.setTerminalCaja(terminalCajaFacade.findTerminalCajaByCodTerminal(entity.getTerminalCaja().getId()));

            entity.setCodTransaccion("003000");
            entity.setStatusTransaccion("O");
            entity.setTipoDispositivo("2");
            super.create(entity);

            VoucherClass voucherClass = converter.castToVoucher(tramaRespuestaReverso);

            if (voucherClass.getCodigoRespuestaAutorizador().equals("00")) {
                entity.setCodAutorizacion(voucherClass.getNumeroAutorizacion());
                entity.setEstadoAutorizacion(EstadoAutorizacion.REVERSO_AUTOMATICO);
                entity.setFechaActualizacion(new Date());
                entity.setEstado(EstadoEntity.MODIFICADA);
                super.create(entity);
                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), "REVERSO AUTOMATICO REALIZADO (TRX PREVIA CON ERROR)"), MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), "REVERSO AUTOMATICO REALIZADO (TRX PREVIA CON ERROR)"), MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(AutorizacionFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), ex.toString()), MediaType.APPLICATION_JSON).build();
        }
    }

    private Response validarUltimaTRX() throws IOException {
        Response resp = null;
        ObjectMapper mapper = new ObjectMapper();
        String temp0 = Files.readAllLines(Paths.get(System.getProperty("jboss.server.config.dir") + "/Temporal.txt")).get(0);
        String temp1 = Files.readAllLines(Paths.get(System.getProperty("jboss.server.config.dir") + "/Temporal.txt")).get(1);
        String stringTEnvio = temp0.replace("tramaEnvio:", "");
        String stringAuxiliar = temp1.replace("auxiliar:", "");
        try {
            AutorizacionAuxiliar aa = mapper.readValue(stringAuxiliar, AutorizacionAuxiliar.class);
            Autorizacion a = converter.castToAutorizacion(aa);
            Voucher voucherTemp = new Voucher();
            String tramaEnvio = stringTEnvio;
            voucherTemp.setTramaEnvio(tramaEnvio);

            resp = postReversarTransaccion2(aa, voucherTemp, stringTEnvio, a);
            MsgFile.deleteFile();

            return resp;

        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), "ERROR EN LA VALIDACIÓN"), MediaType.APPLICATION_JSON).build();
        }
    }

    private Response procesarTRX(AutorizacionAuxiliar auxiliar) {
        try {
            Autorizacion entity = converter.castToAutorizacion(auxiliar);
            entity.setComercio(comercioFacade.findComercioByCodigoComercioBA(entity.getComercio().getId()));
            entity.setTerminalCaja(terminalCajaFacade.findTerminalCajaByCodTerminal(entity.getTerminalCaja().getId()));

            entity.setCodTransaccion("003000");
            entity.setStatusTransaccion("O");
            entity.setTipoDispositivo("2");
            super.create(entity);

            String tipoTrans = auxiliar.getTipoDiferido() == 0 ? TipoTransaccion.CORRIENTE.getTipoTransaccion() : TipoTransaccion.DIFERIDO.getTipoTransaccion();

            voucher = colaAlmacenes.EnviarMensajePago(auxiliar, entity, auxiliar.getConsumos(), tipoTrans);
            System.out.println("MENSAJE123: " + voucher.getTramaRespuesta());

//            voucher = colaAlmacenes.EnviarMensajePago(auxiliar, entity, auxiliar.getConsumos(), tipoTrans);
            System.out.println("-----------------------------------VOUCHER:" + voucher);

            String respuesta = converter.getCodigoRespuesta(voucher.getTramaRespuesta());

            System.out.println("-----------------------------------RESPUESTA: " + respuesta);
            if (respuesta.compareTo(RespuestaEnum.FAIL.getCodigoRespuesta()) == 0) {
                try {
                    System.out.println("-----------------------------ERROR 0001");
                    System.out.println("-----------------------------respuesta: " + respuesta);
                    System.out.println("-----------------------------RespuestaEnum.ERROR.getCodigoRespuesta(): " + RespuestaEnum.ERROR.getCodigoRespuesta());
                    entity.setCodigoRespuesta("0001");
                    entity.setFechaActualizacion(new Date());
                    entity.setEstado(EstadoEntity.MODIFICADA);
                    entity.setEstadoAutorizacion(EstadoAutorizacion.INCONSISTENTE);
                    entity.setDescripcionRespuesta("ERROR-DEBE REVERSAR");
                    System.out.println("-----------------------------ERROR-DEBE REVERSAR");
                    System.out.println("-----------------------------ANTES DE EDIT");
                    super.edit(entity);
                    System.out.println("-----------------------------DESPUES DE EDIT");
                    System.out.println("-----------------------------ANTES DE REVERSAR");
                    Response resp = postReversarTransaccion(auxiliar, voucher, voucher.getTramaEnvio(), entity);
                    System.out.println("-----------------------------DESPUES DE REVERSAR");
                    System.out.println("-----------------------------RESP: " + resp);
                    return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), "ERROR-DEBE REVERSAR"), MediaType.APPLICATION_JSON).build();

                } catch (Exception e) {
                    System.err.println("ERROR: " + e.getMessage());
                    e.printStackTrace();
                    return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), "ERROR-DEBE REVERSAR--------------------CATCH___ERROR"), MediaType.APPLICATION_JSON).build();
                }
            } else {
                String codigoRespuesta = obtenerCodigoRespuestaTransaccion(voucher.getTramaRespuesta());
                entity.setCodigoRespuesta(codigoRespuesta);
                String codigoAdquirienteTarjeta = obtenerCodigoAdquiriente(voucher.getTramaRespuesta());
                entity.setCodAdquirienteTarjeta(codigoAdquirienteTarjeta);
                entity.setCodAdquirienteServicio(codigoAdquirienteTarjeta);

                super.edit(entity);

                if (voucher.getTramaRespuesta().compareTo("java.net.SocketTimeoutException: Read timed out") == 0) {

                    return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.FAIL.getCodigoRespuesta(), RespuestaEnum.FAIL.getDescripcionRespuesta(), "ERROR"), MediaType.APPLICATION_JSON).build();
                }

                VoucherClass voucherClass = converter.castToVoucher(voucher.getTramaRespuesta());

                if (voucherClass.mensajeRespuesta.compareTo("NO ENCONTRADO") != 0) {
                    entity.setSecuencial(voucherClass.getSecuencialTransaccion());
                    voucher.setSecuencial(voucherClass.getSecuencialTransaccion());

                    voucher.setTramaEnvio(EncriptaDesencriptarTrama.Encriptar(voucher.getTramaEnvio()));
                    voucher.setTramaRespuesta(EncriptaDesencriptarTrama.Encriptar(voucher.getTramaRespuesta()));
                    String numeroTarjeta = obtenerNumeroTarjeta(EncriptaDesencriptarTrama.Desencriptar(voucher.getTramaRespuesta()));
                    voucher.setNumeroTarjeta(numeroTarjeta);
                    voucherController.create(voucher);
//                if (voucherClass.getCodigoRespuestaAutorizador().equals("00")
//                        && voucherClass.getNumeroAutorizacion().compareTo("      ") != 0) {
                    if (voucherClass.getCodigoRespuestaAutorizador().equals("00")) {
                        entity.setCodAutorizacion(voucherClass.getNumeroAutorizacion());
                        entity.setEstadoAutorizacion(EstadoAutorizacion.NORMAL);
                        entity.setFechaActualizacion(new Date());
                        entity.setEstado(EstadoEntity.MODIFICADA);
                        entity.setTotalAutoriza(entity.getTotalVenta());
                        entity.setInteres(converter.castValorToBigDecimal(voucherClass.getValorInteres()));
                        entity.setDescripcionRespuesta(voucherClass.getMensajeRespuesta());
//                    entity.setIdLote(entity.getTerminalCaja().getPinpadPrincipal().getLote().get(0).getId());
                        entity.setIdLote(entity.getTerminalCaja().getPinpadPrincipal().getLote().get(entity.getTerminalCaja().getPinpadPrincipal().getLote().size() - 1).getId());
                        super.create(entity);

                        loteFacade.updateLote(entity.getTerminalCaja().getPinpadPrincipal().getLote().get(0));

                    return Response.ok(converter.castToRespuestaCompleta(converter.obtenerCodigoError(EncriptaDesencriptarTrama.Desencriptar(voucher.getTramaRespuesta())), converter.obtenerDescripcionError(EncriptaDesencriptarTrama.Desencriptar(voucher.getTramaRespuesta())), converter.castToVoucher(EncriptaDesencriptarTrama.Desencriptar(voucher.getTramaRespuesta()))), MediaType.APPLICATION_JSON).build();
//                        return Response.ok(converter.castToRespuestaCompleta(converter.obtenerCodigoError((voucher.getTramaRespuesta())), converter.obtenerDescripcionError((voucher.getTramaRespuesta())), converter.castToVoucher((voucher.getTramaRespuesta()))), MediaType.APPLICATION_JSON).build();
                    } else {
                        entity.setCodAutorizacion(null);
                        entity.setFechaActualizacion(new Date());
                        entity.setEstado(EstadoEntity.MODIFICADA);
                        entity.setEstadoAutorizacion(EstadoAutorizacion.INCONSISTENTE);
                        entity.setIdLote(entity.getTerminalCaja().getPinpadPrincipal().getLote().get(0).getId());
                        entity.setDescripcionRespuesta(voucherClass.getMensajeRespuesta());
                        super.create(entity);
                        loteFacade.updateLote(entity.getTerminalCaja().getPinpadPrincipal().getLote().get(0));

                        return Response.ok(converter.castToRespuestaCompleta(converter.obtenerCodigoError(EncriptaDesencriptarTrama.Desencriptar(voucher.getTramaRespuesta())), converter.obtenerDescripcionError(EncriptaDesencriptarTrama.Desencriptar(voucher.getTramaRespuesta())), converter.castToVoucher(EncriptaDesencriptarTrama.Desencriptar(voucher.getTramaRespuesta()))), MediaType.APPLICATION_JSON).build();
                    }
                } else if (voucher.getTramaRespuesta().contains("YYERROR DURANTE PROCE.")) {
                    System.out.println("MENSAJE321");
                    return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), "ERROR DURANTE PROCESO-NO HUBO TRANSMISION"), MediaType.APPLICATION_JSON).build();
                } else {
                    Response resp = postReversarTransaccion(auxiliar, voucher, voucher.getTramaEnvio(), entity);
                    return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), "ERROR EN TABLA PARÁMETROS"), MediaType.APPLICATION_JSON).build();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AutorizacionFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(converter.castToRespuestaCompleta(RespuestaEnum.ERROR.getCodigoRespuesta(), RespuestaEnum.ERROR.getDescripcionRespuesta(), ex.getCause().toString()), MediaType.APPLICATION_JSON).build();
        }
    }

}
