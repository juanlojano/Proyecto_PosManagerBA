/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.utility;

import com.baustro.classes.VoucherClass;
import com.baustro.model.Autorizacion;
import com.baustro.model.AutorizacionAuxiliar;
import com.baustro.model.BaseConsumo;
import com.baustro.model.EstadoAutorizacion;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Lote;
import com.baustro.model.Parametros;
import com.baustro.model.Respuesta;
import com.baustro.model.RespuestaProcesoControl;
import com.baustro.model.RespuestaSimple;
import com.baustro.model.TerminalPinPad;
import com.baustro.model.Voucher;
import com.baustro.sessionbean.BaseConsumoFacade;
import com.baustro.sessionbean.LoteFacade;
import com.baustro.sessionbean.ParametrosFacade;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.lectora.cnx.EnvioSeg;

/**
 *
 * @author ue01000632
 * @param <T>
 */
@Named("ConverterUtilities")
@RequestScoped
public class ConverterUtilities {

    @Inject
    private BaseConsumoFacade baseConsumo;

    @Inject
    private LoteFacade loteFacade;

    @Inject
    private ParametrosFacade parametroFacade;

    public String castToProcesoControl(TerminalPinPad pinpad, int loteNuevo) {
        String procesoControl = "0077";
        String tipoMensaje = "PC";
        String secuencial = "000001";
        String lote = getNumeroLote(loteFacade.FindLoteByPinpad(pinpad.getId()), loteNuevo);
        String filler = "            ";
        String MID = getMid(pinpad.getComercio().getCodComercio() + "   ");
        String TID = getTid(pinpad.getTid());
        String fill = "                       ";
        String CID = "               ";
        String llave = "01AB6463DD21100A1545237D4D746C02";
        procesoControl += tipoMensaje + lote + secuencial + filler + MID + TID;
        procesoControl += fill + CID + llave;
        System.out.println("trama cierre");
        System.out.println(procesoControl);
        return procesoControl;

    }

    public RespuestaProcesoControl castToRespuestaProcesoControl(String tramaRespuesta) {
        RespuestaProcesoControl respuesta = new RespuestaProcesoControl();
        respuesta.setTipoMensaje(tramaRespuesta.substring(4, 6));
        respuesta.setCodigoRespuesta(tramaRespuesta.substring(6, 8));
        respuesta.setMensajeRespuesta(tramaRespuesta.substring(8, 28));
        return respuesta;
    }

    public Autorizacion castToAutorizacion(AutorizacionAuxiliar auxiliar) {
        Autorizacion autorizacion = new Autorizacion();
        autorizacion.setTerminalCaja(auxiliar.getTerminalCaja());
        autorizacion.setComercio(auxiliar.getComercio());
        autorizacion.setTotalVenta(auxiliar.getTotalVenta());
        autorizacion.setTipoDiferido(auxiliar.getTipoDiferido());
        autorizacion.setPlazoDiferido(auxiliar.getPlazoDiferido());
        autorizacion.setSecuencial(auxiliar.getSecuencial());
        autorizacion.setIva(auxiliar.getIva());
        autorizacion.setFechaCreacion(new Date());
        autorizacion.setInteres(auxiliar.getInteres());
        autorizacion.setIdTransaccion(auxiliar.getIdTransaccion());
        autorizacion.setMesesGracia(auxiliar.getMesesGracia());
        autorizacion.setEstado(EstadoEntity.CREADA);
        autorizacion.setEstadoAutorizacion(EstadoAutorizacion.INCONSISTENTE);
        if (auxiliar.getValorPropina() == null) {
            autorizacion.setValorPropina(BigDecimal.ZERO);
        } else {
            autorizacion.setValorPropina(auxiliar.getValorPropina());
        }

        if (auxiliar.getValorServicio() == null) {
            autorizacion.setValorServicio(BigDecimal.ZERO);
        } else {
            autorizacion.setValorServicio(auxiliar.getValorServicio());
        }

        for (BaseConsumo baseconsumo : auxiliar.getConsumos()) {
            if (baseconsumo.getTarifa().compareTo("0") == 0) {
                autorizacion.setMontoBaseNoGrabaIVA(baseconsumo.getValor());
            }
            if (baseconsumo.getTarifa().compareTo("12") == 0) {
                autorizacion.setMontoBaseGrabaIVA(baseconsumo.getValor());
            }
        }

        return autorizacion;
    }

    public String castReverso(Autorizacion autorizacion) {
        System.out.println("inicio reverso");
        BigDecimal montoGravaIva = getConsumo(autorizacion.getFactura().getBaseConsumos(), "12").getValor();
        BigDecimal montoNoGravaIva = getConsumo(autorizacion.getFactura().getBaseConsumos(), "0").getValor();
        montoGravaIva = montoGravaIva == null ? new BigDecimal(0.00) : montoGravaIva;
        montoNoGravaIva = montoNoGravaIva == null ? new BigDecimal(0.00) : montoNoGravaIva;
        String tramaReverso = "00D4";
        String tipoMensaje = "PP";
        String tipoTransaccion = "04";
        String codigoAdquirente = "3";
        String codigoDiferido = "00";
        String plazoDiferido = "  ";
        String mesesGracia = "  ";
        String filler = " ";
        String montoTotalTransaccion = castMontosToString(autorizacion.getTotalVenta());
        String montoBaseGravaIva = castMontosToString(montoGravaIva);
        String montoBaseNoGravaIva = castMontosToString(montoNoGravaIva);
        String impuestoIvaTransaccion = castMontosToString(autorizacion.getFactura().getIva());
        String impuestoServicioTransaccion = "            ";
        String propinaTransaccion = "            ";
        String montoFijo = "            ";
        String secuencialTransaccion = "      ";
        String horaTransaccion = getHora(autorizacion.getFactura().getFecha());
        String fechaTransaccion = getFecha(autorizacion.getFactura().getFecha());
        String numeroAutorizacion = "      ";
        String MID = getMid(autorizacion.getComercio().getCodComercio() + "   ");
        String TID = getTid(autorizacion.getTerminalCaja().getPinpadPrincipal().getTid());
        String CID = "               ";
        String Filler = "                    ";
        String llave = "01AB6463DD21100A1545237D4D746C02";

        tramaReverso += tipoMensaje + tipoTransaccion + codigoAdquirente;
        tramaReverso += codigoDiferido + plazoDiferido + mesesGracia + filler + montoTotalTransaccion;
        tramaReverso += montoBaseGravaIva + montoBaseNoGravaIva + impuestoIvaTransaccion;
        tramaReverso += impuestoServicioTransaccion + propinaTransaccion;
        tramaReverso += montoFijo + secuencialTransaccion + horaTransaccion + fechaTransaccion + numeroAutorizacion;
        tramaReverso += MID + TID + CID + Filler + llave;
        System.out.println("tramaReverso*******************");
        System.out.println(tramaReverso);
        return tramaReverso;
    }

    public String obtenerCodigoError(String tramaRespuesta) {
        String CRM = tramaRespuesta.substring(6, 8);
        String CRA = tramaRespuesta.substring(10, 12);
        if (CRM.compareTo("00") == 0 && CRA.compareTo("00") == 0) {

            return CRM;
        }
        if (CRM.compareTo("00") == 0 && CRA.compareTo("00") != 0) {

            return CRA;
        }
        if (CRM.compareTo("00") != 0 && CRA.compareTo("00") == 0) {

            return CRM;
        }

        return "";
    }

    public String obtenerDescripcionError(String tramaRespuesta) {

        return tramaRespuesta.substring(12, 32);
    }

    public String obtenerBinTarjeta(String tramaRespuesta) {

        return tramaRespuesta.substring(398, 423);
    }

    public VoucherClass castToVoucher(String tramaRespuesta) {
        VoucherClass voucher = new VoucherClass();
        voucher.tipoMensaje = tramaRespuesta.substring(4, 6);
        voucher.codigoRespuestaMensaje = tramaRespuesta.substring(6, 8);
        voucher.codigoRedAdquirente = tramaRespuesta.substring(8, 10);
        if (tramaRespuesta.contains("CANCELADA")) {
            voucher.codigoRespuestaAutorizador = "TC";
            voucher.mensajeRespuesta = "TRANSACCION CANCELADA";
        } else {
            voucher.codigoRespuestaAutorizador = tramaRespuesta.substring(10, 12);
            voucher.mensajeRespuesta = getDetalleRespuesta(tramaRespuesta);
        }
        voucher.secuencialTransaccion = tramaRespuesta.substring(32, 38);
        voucher.numeroLote = tramaRespuesta.substring(38, 44);
        voucher.horaTransaccion = tramaRespuesta.substring(44, 50);
        voucher.fechaTransaccion = tramaRespuesta.substring(50, 58);
        voucher.numeroAutorizacion = tramaRespuesta.substring(58, 64);
        voucher.terminalId = tramaRespuesta.substring(64, 72);
        voucher.merchantId = tramaRespuesta.substring(72, 87);
        voucher.valorInteres = tramaRespuesta.substring(87, 99);
        voucher.mensajeImpresionPublicidad = tramaRespuesta.substring(99, 179);
        voucher.codigoBancoAdquiriente = tramaRespuesta.substring(179, 182);
        voucher.nombreBancoAdquirente = tramaRespuesta.substring(182, 212);
        voucher.nombreGrupoTarjeta = tramaRespuesta.substring(212, 237);
        voucher.modoLectura = tramaRespuesta.substring(237, 239);
        voucher.nombreTarjetaHabiente = tramaRespuesta.substring(239, 279);
        voucher.montoFijo = tramaRespuesta.substring(279, 291);
        voucher.identificacionEMV = tramaRespuesta.substring(291, 311);
        voucher.aid_emv = tramaRespuesta.substring(311, 331);
        voucher.tipoCriptografia = tramaRespuesta.substring(331, 353);
        voucher.verificacionPin = tramaRespuesta.substring(353, 368);
        voucher.arqc = tramaRespuesta.substring(368, 384);
        voucher.tvr = tramaRespuesta.substring(384, 394);
        voucher.tsi = tramaRespuesta.substring(394, 398);
        voucher.numeroTarjetaTruncado = tramaRespuesta.substring(398, 423);
        voucher.fechaVencimientoTarjeta = tramaRespuesta.substring(423, 427);
        voucher.numeroTarjetaEncriptado = tramaRespuesta.substring(427, 491);

        System.out.println("Voucher ****************");

        System.out.println("tipoMensaje " + voucher.tipoMensaje);
        System.out.println("codigoRespuesta " + voucher.codigoRespuestaMensaje);
        System.out.println("codigoRedAdquirente " + voucher.codigoRedAdquirente);
        System.out.println("codigoRespuestaAutorizador " + voucher.codigoRespuestaAutorizador);
        System.out.println("mensajeRespuesta " + voucher.mensajeRespuesta);
        System.out.println("seceuncial transaccion " + voucher.secuencialTransaccion);
        System.out.println("numeroLote " + voucher.numeroLote);
        System.out.println("horatransaccion " + voucher.horaTransaccion);
        System.out.println("fechatransaccion " + voucher.fechaTransaccion);
        System.out.println("numeroautorizacion " + voucher.numeroAutorizacion);
        System.out.println("terminal id " + voucher.terminalId);
        System.out.println("merchantid " + voucher.merchantId);
        System.out.println("valorinteres " + voucher.valorInteres);
        System.out.println("mensjaeImpresionPublicidad " + voucher.mensajeImpresionPublicidad);
        System.out.println("codigobancoadquiirent " + voucher.codigoBancoAdquiriente);
        System.out.println("nombre banco adquirente " + voucher.nombreBancoAdquirente);
        System.out.println("nombregrupotarjeta " + voucher.nombreGrupoTarjeta);
        System.out.println("modolectura " + voucher.modoLectura);
        System.out.println("nombretarjetahabiente " + voucher.nombreTarjetaHabiente);
        System.out.println("monto fijo " + voucher.montoFijo);
        System.out.println("identificacionEMV " + voucher.identificacionEMV);
        System.out.println("aid_emv " + voucher.aid_emv);
        System.out.println("tipocriptografia " + voucher.tipoCriptografia);
        System.out.println("verificacinPin " + voucher.verificacionPin);
        System.out.println("arqc " + voucher.arqc);
        System.out.println("tvr " + voucher.tvr);
        System.out.println("tsi " + voucher.tsi);
        System.out.println("numero tarjeta truncado " + voucher.numeroTarjetaTruncado);
        System.out.println("fechavencimiento tarjeta " + voucher.fechaVencimientoTarjeta);
        System.out.println("numero tarjeta encriptado " + voucher.numeroTarjetaEncriptado);

        return voucher;

    }

    public String castProcesoPago(Autorizacion autorizacion, List<BaseConsumo> consumos, String tipoTrans) throws Exception {
        BigDecimal montoGravaIva = getConsumo(consumos, "12").getValor();
        BigDecimal montoNoGravaIva = getConsumo(consumos, "0").getValor();
        montoGravaIva = montoGravaIva == null ? new BigDecimal(0.00) : montoGravaIva;
        montoNoGravaIva = montoNoGravaIva == null ? new BigDecimal(0.00) : montoNoGravaIva;
        String tramaEnvio = "00D4";
        String tipoMensaje = "PP";
        //String tipoTransaccion = "01";
        String tipoTransaccion = tipoTrans;
        String codigoAdquirente = "3";
        String codigoDiferido = castTipoDiferido(autorizacion.getTipoDiferido());
        String plazoDiferido = castPlazoDiferidoToString(autorizacion.getPlazoDiferido());
        String mesesGracia = castMesesGracia(autorizacion.getMesesGracia());
        String filler = " ";
        //String montoTotalTransaccion = "000000007623";
        String montoTotalTransaccion = castMontosToString(autorizacion.getTotalVenta());
        //String montoBaseGravaIva = "000000000010";        
        String montoBaseGravaIva = castMontosToString(montoGravaIva);
        //String montoBaseNoGravaIva = "000000007612";        
        String montoBaseNoGravaIva = castMontosToString(montoNoGravaIva);
        //String impuestoIvaTransaccion = "000000000001";
        String impuestoIvaTransaccion = castMontosToString(autorizacion.getIva());
        String impuestoServicioTransaccion = "            ";
        String propinaTransaccion = "            ";
        String montoFijo = "            ";
        String secuencialTransaccion = "      ";
        //String horaTransaccion = "101740";
        String horaTransaccion = getHora(autorizacion.getFechaCreacion());
        //String fechaTransaccion = "20171012";
        String fechaTransaccion = getFecha(autorizacion.getFechaCreacion());
        String numeroAutorizacion = "      ";
        String MID = getMid(autorizacion.getComercio().getCodComercio() + "   ");
        String TID = getTid(autorizacion.getTerminalCaja().getPinpadPrincipal().getTid());
        String CID = "               ";
        String Filler = "                    ";
        //String llave = "01AB6463DD21100A1545237D4D746C02";

        //secuencialTransaccion = tipoTransaccion.equals(TipoTransaccion.ANULACION.getTipoTransaccion()) ? "000147" : "      ";
        //numeroAutorizacion = tipoTransaccion.equals(TipoTransaccion.ANULACION.getTipoTransaccion()) ? "087032" : "      ";
        //tipoTransaccion = tipoTransaccion.equals(TipoTransaccion.ANULACION.getTipoTransaccion()) ? "03" : "01";
        tramaEnvio += tipoMensaje + tipoTransaccion + codigoAdquirente;
        tramaEnvio += codigoDiferido + plazoDiferido + mesesGracia + filler + montoTotalTransaccion;
        tramaEnvio += montoBaseGravaIva + montoBaseNoGravaIva + impuestoIvaTransaccion;
        tramaEnvio += impuestoServicioTransaccion + propinaTransaccion;
        tramaEnvio += montoFijo + secuencialTransaccion + horaTransaccion + fechaTransaccion + numeroAutorizacion;
        tramaEnvio += MID + TID + CID + Filler; //+ llave;
        tramaEnvio = EnvioSeg.Genera_Componente(tramaEnvio, 0);
        System.out.println("tramaenvio*******************");
        System.out.println(tramaEnvio);
        return tramaEnvio;
    }

    public String castMontosToString(BigDecimal montoTotal) {
        String total = null;
        BigInteger tot;
        tot = montoTotal.multiply(new BigDecimal("100")).toBigInteger();
        total = "0000000000000000".concat(tot.toString());
        total = total.substring(total.length() - 12, total.length());
        return total;
    }

    public String castPlazoDiferidoToString(int plazoDiferido) {
        String plazo = null;
        if (plazoDiferido == 0) {
            plazo = "  ";
        } else {
            plazo = "00".concat(String.valueOf(plazoDiferido));
            plazo = plazo.substring(plazo.length() - 2, plazo.length());
        }

        return plazo;
    }

    public BaseConsumo getConsumo(List<BaseConsumo> consumos, String tarifa) {
        BaseConsumo bConsumo = new BaseConsumo();
        for (BaseConsumo bc : consumos) {
            if (bc.getTarifa().equals(tarifa)) {
                bConsumo = bc;
            }
        }
        return bConsumo;
    }

    public String getHora(Date fecha) {
        DateFormat df = new SimpleDateFormat("HHmmss");
        String reportDate = df.format(fecha);
        return reportDate;
    }

    public String getDetalleRespuesta(String trama) {
        ParametrosFacade pf = new ParametrosFacade(); 
        Parametros mensajeRsptaFromParametro = pf.findByCodigoRespuesta(trama.substring(10, 12));

        if (mensajeRsptaFromParametro != null) {
            return mensajeRsptaFromParametro.getParamDescripcion();
        } else {
            return "NO ENCONTRADO";
        }
    }

    public Boolean detalleRespuestaEncontrado(String trama) {
        Parametros mensajeRsptaFromParametro = parametroFacade.findByCodigoRespuesta(trama.substring(10, 12));
        if (mensajeRsptaFromParametro != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getCodigoRespuesta(String trama) {
        String codigoRespuestaAut = trama.substring(10, 12);

        return codigoRespuestaAut;
    }

    public String getMid(String mid) {
        String aux = "000000000000".concat(mid);
        return aux.substring(aux.length() - 15, aux.length());
    }

    public String getTid(String tid) {
        String aux = "000000000000" + tid;
        return aux.substring(aux.length() - 8, aux.length());

    }

    public String getNumeroLote(Lote lote, int loteNuevo) {
        String respuesta = "0000000";
        respuesta = respuesta.concat(String.valueOf(loteNuevo));
        return respuesta.substring(respuesta.length() - 6, respuesta.length());
    }

    /*
    public BigDecimal castInteres(String interes) {
        if (interes.trim().equals("")) {
            return new BigDecimal("0");
        } else {

            int decimal = Integer.parseInt(interes.substring(interes.length() - 2));
            int entero = Integer.parseInt(interes.substring(0, interes.length() - 2));
            String inter = Integer.toString(entero).concat(".").concat(Integer.toString(decimal));
            return new BigDecimal(inter);
        }
    }*/
 /*
    public BigDecimal castTotalAutoriza(String total) {
        if (total.trim().equals("")) {
            return new BigDecimal("0.00");
        } else {
            int decimal = Integer.parseInt(total.substring(total.length() - 2));
            int entero = Integer.parseInt(total.substring(0, total.length() - 2));
            //BigDecimal d = new BigDecimal(entero+"."+decimal);
            String tot = Integer.toString(entero).concat(".").concat(Integer.toString(decimal));

            return new BigDecimal(tot);
        }
    }
     */
    public BigDecimal castValorToBigDecimal(String total) {
        if (total.trim().equals("")) {
            return new BigDecimal("0.00");
        } else {
            int decimal = Integer.parseInt(total.substring(total.length() - 2));
            int entero = Integer.parseInt(total.substring(0, total.length() - 2));
            String tot = Integer.toString(entero).concat(".").concat(Integer.toString(decimal));

            return new BigDecimal(tot);
        }
    }

    public String getFecha(Date fecha) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String reportDate = df.format(fecha);
        return reportDate;
    }

    public String castTipoDiferido(int tDiferido) {
        String respuesta = "0000";
        respuesta = respuesta.concat(Integer.toString(tDiferido));
        respuesta = respuesta.substring(respuesta.length() - 2, respuesta.length());
        return respuesta;
    }

    public String castMesesGracia(int mGracia) {
        String respuesta = "    ";
        if (mGracia == 0) {
            respuesta = respuesta.substring(respuesta.length() - 2, respuesta.length());
        } else {
            respuesta = "0000".concat(Integer.toString(mGracia));
            respuesta = respuesta.substring(respuesta.length() - 2, respuesta.length());
        }
        return respuesta;
    }

    public Respuesta castToRespuestaCompleta(String codigo, String descripion, Object objeto) {
        Respuesta respuesta = new Respuesta();
        respuesta.setCodigoRespuesta(codigo);;
        respuesta.setDescripcionRespuesta(descripion);
        respuesta.setObjetoRespuesta(objeto);

        return respuesta;
    }

    public RespuestaSimple castToRespuestaSimple(Object objeto) {
        RespuestaSimple respuestaSimple = new RespuestaSimple();
        respuestaSimple.setObjetoRespuesta(objeto);

        return respuestaSimple;
    }

    public String castTramaIngresoToAutorizacion(String tramaEnvio, String codAuth, String referencia) {
        String tramaAnulacion = "";
        String codigoAnulacion = tramaEnvio.substring(0, 6);
        String secuencialAnulacion = tramaEnvio.substring(8, 100);
        String autorizacionAnulacion = tramaEnvio.substring(106, 120);
        String finTrama = tramaEnvio = tramaEnvio.substring(126, tramaEnvio.length());
        tramaAnulacion = codigoAnulacion + "03" + secuencialAnulacion + referencia + autorizacionAnulacion + codAuth + finTrama;
        return tramaAnulacion;
    }

    public String castTramaIngresoToReverso(String tramaRev) {
        String tramaReverso = "";
        String codigoReverso = tramaRev.substring(0, 7);
        String finalTrama = tramaRev.substring(8, tramaRev.length());
        tramaReverso = codigoReverso + "4" + finalTrama;
        return tramaReverso;
    }
}
