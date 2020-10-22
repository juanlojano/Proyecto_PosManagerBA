/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.bean.almacenes;

import com.baustro.mensajeria.jms_cola_mensajeria.C_M_MensajeConsumer;
import com.baustro.mensajeria.jms_cola_mensajeria.C_M_MensajeProducer;
import com.baustro.model.Autorizacion;
import com.baustro.model.AutorizacionAuxiliar;
import com.baustro.model.BaseConsumo;
import com.baustro.model.Comercio;
import com.baustro.model.EstadoEntity;
import com.baustro.model.EstadoLote;
import com.baustro.model.Lote;
import com.baustro.model.RespuestaProcesoControl;
import com.baustro.model.TerminalPinPad;
import com.baustro.model.TipoTransaccion;
import com.baustro.model.Voucher;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.BinTarjetaFacade;
import com.baustro.sessionbean.LoteFacade;
import com.baustro.sessionbean.TerminalCajaFacade;
import com.baustro.sessionbean.VoucherFacade;
import com.baustro.util.MsgFile;
import com.baustro.utility.ConverterUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javassist.CtMethod.ConstParameter.string;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ue01000632
 */
//@Singleton
@Stateless
public class ManejadorColaAlmacenes {

    Random rnd = new Random();
    Integer secuencialTransaccion = 0;
    Integer secuencialAutorizacion = 0;
    String dataTxt_Temp = "";
    //ConverterUtilities converter = new ConverterUtilities();

    @Inject
    private ConverterUtilities converter;

    @Inject
    private C_M_MensajeProducer c_m_mensajeProducer;

    @Inject
    private C_M_MensajeConsumer c_m_mensajeConsumer;

    @Inject
    private LoteFacade loteFacade;

    @Inject
    private BinTarjetaFacade binTarjetaFacade;

    @Inject
    private AutorizacionFacade autorizacionFacade;

    @Inject
    private VoucherFacade voucherFacade;

    public String temporal;

//    public Voucher EnviarMensajePago(AutorizacionAuxiliar aut_auxiliar, Autorizacion autorizacion, List<BaseConsumo> consumos, String tipoTransaccion) throws JRException, FileNotFoundException, Exception {
    public Voucher EnviarMensajePago(AutorizacionAuxiliar aut_auxiliar, Autorizacion autorizacion, List<BaseConsumo> consumos, String tipoTransaccion) throws JRException, FileNotFoundException, Exception {

        MsgFile.deleteFile();

        Voucher voucher = new Voucher();
        String respuesta = "";
        String tramaEnvio = autorizacion.getTipoDiferido() != 0 ? converter.castProcesoPago(autorizacion, consumos, "02") : tipoTransaccion.equals(TipoTransaccion.ANULACION.getTipoTransaccion()) ? converter.castProcesoPago(autorizacion, consumos, "03") : converter.castProcesoPago(autorizacion, consumos, "01");
        voucher.setTramaEnvio(tramaEnvio);

        MsgFile.write("tramaEnvio:" + tramaEnvio);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(aut_auxiliar);
        MsgFile.write("auxiliar:" + jsonInString);

//        mensajeProducerVoucher.consumerString(voucher);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonString = objectMapper.writeValueAsString(aut_auxiliar);
//        c_m_mensajeProducer.sendMensaje(jsonString + "/" + tramaEnvio);
//        respuesta = c_m_mensajeConsumer.onMessage();
//        MsgFile.write("respuesta:" + respuesta);
//        System.out.println("-------------------------MENSAJE RECIBIDO : " + respuesta);
        respuesta = EnviarTramaPago(autorizacion, tramaEnvio);
        System.out.println("RESPUESTA: " + respuesta);
        MsgFile.write("respuesta:" + respuesta);

//        String binTarjeta = converter.obtenerBinTarjeta(respuesta);
//        if (binTarjetaFacade.findBinTarjetaByBin(binTarjeta)) {
//            voucher.setAutorizacion(autorizacion);
//            voucher.setTramaRespuesta(respuesta);
//
//            return voucher;
//        } else {
//            return null;
//        }
        voucher.setAutorizacion(autorizacion);
        voucher.setTramaRespuesta(respuesta);

        return voucher;
    }

    public String EnviarMensajeAnulacion(AutorizacionAuxiliar autorizacion, String tramaAnulacion) throws JRException, FileNotFoundException {

        String respuesta = "";
        respuesta = EnviarTramaAnulacion(autorizacion, tramaAnulacion);

        return respuesta;
    }

    public String EnviarMensajeAnulacionPosmanager(Autorizacion autorizacion, String tramaAnulacion) throws JRException, FileNotFoundException {
        String respuesta = "";
        respuesta = EnviarTramaAnulacionPosmanager(autorizacion, tramaAnulacion);

        return respuesta;
    }

    public String EnviarTramaPago(Autorizacion autorizacion, String tramaEnvio) {
        int lenData = 0;
        String dataTxt = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = tramaEnvio.getBytes();
            Socket socket = new Socket(autorizacion.getTerminalCaja().getPinpadPrincipal().getIp(), autorizacion.getTerminalCaja().getPinpadPrincipal().getPuerto());
            InputStream is = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
//            socket.setSoTimeout(30000);
            out.write(data);
            for (;;) {
                int byteData = is.read();
                lenData = is.available();
                if ((byteData == -1) || (lenData == 0)) {
                    baos.write(byteData);
                    break;
                }
                baos.write(byteData);
            }
            dataTxt = baos.toString();
            System.out.println("-----------------------------------   respuesta pinpad: " + dataTxt);
            socket.close();

            /**
             * Modificación para simulador pinpad (autorizaciones)
             */
            if (dataTxt.length() < 50) {
                String secuencialTransaccionString = devolverSecuencialTransaccion();
                String numeroAutorizacionString = devolverSecuencialAutorizacion();
                dataTxt = "0202PP000300AUTORIZACION OK.    " + secuencialTransaccionString + "00460010174020170920" + numeroAutorizacionString + "612131434000000332976                                                                                                                                VISA                     03MAISINCHO/RODRIGO                       000000000030VISA CREDITO                                                                                 0200008000E800476397XXXXXX5009         2103F145E656952ACAAB0E27E897AC2819586B80F7015F59E3143013A2C4BB134331                           01AB6463DD21100A0A9BD80286CAE7D8";
                dataTxt_Temp = dataTxt;
                System.out.println("respuesta pinpad boolean: " + dataTxt);
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("ERROR: " + ex.getMessage());
            return ex.toString();
        }

        return dataTxt;
    }

    public String EnviarTramaAnulacion(AutorizacionAuxiliar autorizacion, String tramaAnulacion) {
        int lenData = 0;
        String dataTxt = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = tramaAnulacion.getBytes();
            Socket socket = new Socket(autorizacion.getTerminalCaja().getPinpadPrincipal().getIp(), autorizacion.getTerminalCaja().getPinpadPrincipal().getPuerto());
            InputStream is = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            socket.setSoTimeout(30000);
            out.write(data);
            for (;;) {
                int byteData = is.read();
                lenData = is.available();
                if ((byteData == -1) || (lenData == 0)) {
                    baos.write(byteData);
                    break;
                }
                baos.write(byteData);
            }
            dataTxt = baos.toString();
            System.out.println("respuesta pinpad boolean: " + dataTxt);
            socket.close();

            /**
             * Modificación para simulador pinpad (autorizaciones)
             */
            if (dataTxt.length() < 100) {
                dataTxt_Temp = "0202PP000300AUTORIZACION OK.    00000200460010174020170920000002612131434000000332976                                                                                                                                VISA                     03MAISINCHO/RODRIGO                       000000000030VISA CREDITO                                                                                 0200008000E800476397XXXXXX5009         2103F145E656952ACAAB0E27E897AC2819586B80F7015F59E3143013A2C4BB134331                           01AB6463DD21100A0A9BD80286CAE7D8";
                String data1 = dataTxt_Temp.substring(0, 58);
                String data2 = autorizacion.getCodAutorizacion();
                String data3 = dataTxt_Temp.substring(64, dataTxt_Temp.length());
                dataTxt = data1 + data2 + data3;
//                String data3 = "             ";
//                String data4 = data1 + data3 + data2;
//                dataTxt = data4;
                System.out.println("respuesta pinpad boolean: " + dataTxt);
                socket.close();
            }

        } catch (IOException ex) {
//            if(dataTxt.equals("")){
//                dataTxt = tramaAnulacion;
//            }
//            return dataTxt;
            System.err.println("ERROR: " + ex);
            ex.printStackTrace();

            return ex.toString();
        }

        return dataTxt;
    }

    public String EnviarTramaAnulacionPosmanager(Autorizacion autorizacion, String tramaAnulacion) {
        int lenData = 0;
        String dataTxt = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = tramaAnulacion.getBytes();
            Socket socket = new Socket(autorizacion.getTerminalCaja().getPinpadPrincipal().getIp(), autorizacion.getTerminalCaja().getPinpadPrincipal().getPuerto());
            InputStream is = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            socket.setSoTimeout(30000);
            out.write(data);
            for (;;) {
                int byteData = is.read();
                lenData = is.available();
                if ((byteData == -1) || (lenData == 0)) {
                    baos.write(byteData);
                    break;
                }
                baos.write(byteData);
            }
            dataTxt = baos.toString();
            System.out.println("respuesta pinpad boolean: " + dataTxt);
            socket.close();

            /**
             * Modificación para simulador pinpad (autorizaciones)
             */
            if (dataTxt.length() < 100) {
                String data1 = dataTxt_Temp.substring(0, 279);
                String data2 = dataTxt_Temp.substring(292, dataTxt_Temp.length());
                String data3 = "             ";
                String data4 = data1 + data3 + data2;
                dataTxt = data4;
                System.out.println("respuesta pinpad boolean: " + dataTxt);
                socket.close();
            }

        } catch (IOException ex) {
//            if(dataTxt.equals("")){
//                dataTxt = tramaAnulacion;
//            }
//            return dataTxt;
            System.err.println("ERROR: " + ex);
            ex.printStackTrace();

            return ex.toString();
        }

        return dataTxt;
    }

    public String cierrePinPad(TerminalPinPad pinpad) {
        try {
            Lote loteAntiguo = loteFacade.FindLoteByPinpad(pinpad.getId());
            String respuesta = EnviarTramaCierre(pinpad, loteAntiguo.getNumeroLote() + 1);
            System.out.println("respuesta cierre " + respuesta);
            RespuestaProcesoControl respuestaPC = converter.castToRespuestaProcesoControl(respuesta);
            if (respuestaPC.getCodigoRespuesta().equals("00")) {
                loteAntiguo.setEstado(EstadoEntity.MODIFICADA);
                loteAntiguo.setEstadoLote(EstadoLote.CERRADO);
                loteFacade.create(loteAntiguo);
                Lote nuevoLote = new Lote();
                nuevoLote.setEstado(EstadoEntity.CREADA);
                nuevoLote.setEstadoLote(EstadoLote.ABIERTO);
                nuevoLote.setNumeroLote(loteAntiguo.getNumeroLote() + 1);
                nuevoLote.setPinpad(pinpad);
                nuevoLote.setValor(BigDecimal.ZERO);
                loteFacade.create(nuevoLote);
                MsgFile.write(pinpad.getIp() + ":" + "CERRADO");

            } else {
                MsgFile.write(pinpad.getIp() + ":" + "NO CERRADO");

            }
            return respuesta;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String EnviarTramaCierre(TerminalPinPad pinpad, int loteNuevo) {
        int lenData = 0;
        String dataTxt = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = converter.castToProcesoControl(pinpad, loteNuevo).getBytes();
            Socket socket = new Socket(pinpad.getIp(), pinpad.getPuerto());
            InputStream is = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            socket.setSoTimeout(30000);
            out.write(data);
            for (;;) {
                int byteData = is.read();
                lenData = is.available();
                if ((byteData == -1) || (lenData == 0)) {
                    baos.write(byteData);
                    break;
                }
                baos.write(byteData);
            }
            dataTxt = baos.toString();
            System.out.println("respuesta pinpad boolean: " + dataTxt);
            socket.close();
        } catch (IOException ex) {

            return ex.toString();
        }

        return dataTxt;
    }

    private String devolverSecuencialAutorizacion() {
        List<Autorizacion> listaAuth = autorizacionFacade.findAll();
        if (listaAuth.size() == 1) {
            return "000001";
        } else {
            String numAutString = listaAuth.get(listaAuth.size() - 2).getCodAutorizacion();
            System.out.println("----------------: NUM: " + numAutString);
            int numAutInt = Integer.parseInt(numAutString);
            System.out.println("----------------: N: " + numAutInt);
            numAutInt++;
            String numAutStringIncrementado = String.valueOf(numAutInt);
            switch (numAutStringIncrementado.length()) {
                case 0: {

                    return "000001";
                }
                case 1: {

                    return "00000" + numAutStringIncrementado;
                }
                case 2: {

                    return "0000" + numAutStringIncrementado;
                }
                case 3: {

                    return "000" + numAutStringIncrementado;
                }
                case 4: {

                    return "00" + numAutStringIncrementado;
                }
                case 5: {

                    return "0" + numAutStringIncrementado;
                }
                case 6: {

                    return numAutStringIncrementado;
                }
                default:
                    break;
            }
        }

        return null;
    }

    private String devolverSecuencialTransaccion() {
        List<Autorizacion> listaAuth = autorizacionFacade.findAll();
        if (listaAuth.size() == 1) {
            return "000001";
        } else {
            String numAutString = listaAuth.get(listaAuth.size() - 2).getSecuencial();
            System.out.println("----------------: NUM: " + numAutString);
            int numAutInt = Integer.parseInt(numAutString);
            System.out.println("----------------: N: " + numAutInt);
            numAutInt++;
            String numAutStringIncrementado = String.valueOf(numAutInt);
            switch (numAutStringIncrementado.length()) {
                case 0: {

                    return "000001";
                }
                case 1: {

                    return "00000" + numAutStringIncrementado;
                }
                case 2: {

                    return "0000" + numAutStringIncrementado;
                }
                case 3: {

                    return "000" + numAutStringIncrementado;
                }
                case 4: {

                    return "00" + numAutStringIncrementado;
                }
                case 5: {

                    return "0" + numAutStringIncrementado;
                }
                case 6: {

                    return numAutStringIncrementado;
                }
                default:
                    break;
            }
        }

        return null;
    }

    public String getTemporal() {
        return temporal;
    }

    public void setTemporal(String temporal) {
        this.temporal = temporal;
    }

}
