/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.bean;

import com.baustro.model.Autorizacion;
import com.baustro.model.EstadoEntity;
import com.baustro.model.EstadoLote;
import com.baustro.model.Lote;
import com.baustro.model.RespuestaProcesoControl;
import com.baustro.model.TerminalPinPad;
import com.baustro.model.Voucher;
import com.baustro.sessionbean.LoteFacade;
import com.baustro.utility.ConverterUtilities;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.Socket;
import javax.ejb.Singleton;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ue01000632
 */
@Singleton
public class ManejadorCola {

    //ConverterUtilities converter = new ConverterUtilities();
    @Inject
    private ConverterUtilities converter;
    @Inject
    private LoteFacade loteFacade;
//    @Inject
//    private Voucher voucher;
/*
    public Voucher EnviarMensaje(Autorizacion autorizacion) throws JRException, FileNotFoundException {
        Voucher voucher = new Voucher();
        String respuesta = "";
        respuesta = EnviarTrama(autorizacion);
        voucher.setAutorizacion(autorizacion);
        voucher.setTramaRespuesta(respuesta);
        return voucher;
    }

    public String EnviarTrama(Autorizacion autorizacion) {
        Socket smtpSocket = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        String respuesta = "";
        //System.out.println(autorizacion.getTerminalCaja().getPinpadPrincipal().getIp().toString());
        try {
            smtpSocket = new Socket(autorizacion.getTerminalCaja().getPinpadPrincipal().getIp(), autorizacion.getTerminalCaja().getPinpadPrincipal().getPuerto());
            //smtpSocket = new Socket("10.1.141.21",9091);
            os = new DataOutputStream(smtpSocket.getOutputStream());
            is = new DataInputStream(smtpSocket.getInputStream());
            if (smtpSocket != null && os != null && is != null) {
                os.writeBytes(castProcesoPago(autorizacion));
                //os.writeBytes("00D4PP01300     000000007623000000000010000000007612000000000001                                          10174020170928      000000332976   12131434                                   01AB6463DD21100A1545237D4D746C02");
                //System.out.println("tram trama tram trama");                
                String responseLine = "";
                while ((responseLine = is.readLine()) != null) {
                    System.out.println("respuesta " + responseLine);
                    if (responseLine.indexOf("Ok") != -1) {
                        break;
                    } else {
                        respuesta = responseLine;
                    }
                }
                os.close();
                is.close();
                smtpSocket.close();
                return respuesta;
            }
        } catch (Exception e) {
            return e.toString();
        }
        return respuesta;
    }

    
*/
    
}