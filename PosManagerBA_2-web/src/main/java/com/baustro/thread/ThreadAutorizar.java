/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.thread;

import com.baustro.bean.almacenes.ManejadorColaAlmacenes;
import com.baustro.classes.VoucherClass;
import com.baustro.jsfclasses.VoucherController;
import com.baustro.model.Autorizacion;
import com.baustro.model.AutorizacionAuxiliar;
import com.baustro.model.BaseConsumo;
import com.baustro.model.EstadoAutorizacion;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Factura;
import com.baustro.model.RespuestaEnum;
import com.baustro.model.TipoTransaccion;
import com.baustro.model.Voucher;
import com.baustro.service.AutorizacionFacadeREST;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.ComercioFacade;
import com.baustro.sessionbean.TerminalCajaFacade;
import com.baustro.utility.ConverterUtilities;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ba0100063v
 */
public class ThreadAutorizar extends Thread {

    @Inject
    private TerminalCajaFacade terminalCajaFacade;
    @Inject
    private VoucherController voucherController;
    @Inject
    private ComercioFacade comercioFacade;
    @Inject
    private AutorizacionFacade autorizacionFacade;
    @Inject
    private Voucher voucher;
    @Inject
    private ManejadorColaAlmacenes colaAlmacenes;
    @Inject
    private ConverterUtilities converter;

    AutorizacionAuxiliar auxiliar;

    public ThreadAutorizar(AutorizacionAuxiliar auxiliar) {
        this.auxiliar = auxiliar;
    }

    @Override
    public void run() {
        try {
//            metodoCrearAutorizacion();
        } catch (Exception ex) {
            Logger.getLogger(ThreadAutorizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private boolean validarDiferidos(Integer tipoDiferido, Integer plazoDiferido, int mesesGracia) {
        if (tipoDiferido == 0 && plazoDiferido == 0 && mesesGracia == 0) {

            return true;
        }
        if (tipoDiferido == 2 && mesesGracia == 0) {

            return true;
        }

        if (tipoDiferido == 3 && mesesGracia == 0) {

            return true;
        }

        if (tipoDiferido == 5 && mesesGracia == 2) {

            return true;
        }

        if (tipoDiferido == 6 && mesesGracia == 3) {

            return true;
        }

        if (tipoDiferido == 7 && mesesGracia == 1) {

            return true;
        }

        if (tipoDiferido == 8 && mesesGracia == 2) {

            return true;
        }

        return false;
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

    public void metodo() {

    }
}
