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
import com.baustro.model.EstadoAutorizacion;
import com.baustro.model.EstadoEntity;
import com.baustro.model.RespuestaEnum;
import com.baustro.model.TipoTransaccion;
import com.baustro.model.Voucher;
import com.baustro.service.AutorizacionFacadeREST;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.ComercioFacade;
import com.baustro.sessionbean.LoteFacade;
import com.baustro.sessionbean.TerminalCajaFacade;
import com.baustro.sessionbean.VoucherFacade;
import com.baustro.utility.ConverterUtilities;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ba0100063v
 */
public class AutorizacionCallable implements Callable<Voucher> {

    AutorizacionAuxiliar auxiliar;

//    ComercioFacade cf = new ComercioFacade();
//    TerminalCajaFacade tf = new TerminalCajaFacade();
//    AutorizacionFacade af = new AutorizacionFacade();
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
    private AutorizacionFacade autorizacionFacade;
    @Inject
    private VoucherFacade voucherFacade;
    @Inject
    private LoteFacade loteFacade;

    Autorizacion entity;
    String tipoTrans;

    ConverterUtilities cu = new ConverterUtilities();
    VoucherController vc = new VoucherController();

    public AutorizacionCallable() {
    }

    public AutorizacionCallable(AutorizacionAuxiliar auxiliar) {
        this.auxiliar = auxiliar;
    }

    public AutorizacionCallable(AutorizacionAuxiliar auxiliar,
            ConverterUtilities converter,
            ManejadorColaAlmacenes colaAlmacenes,
            Voucher voucher,
            AutorizacionFacade autorizacionFacade,
            LoteFacade loteFacade,
            ComercioFacade comercioFacade,
            TerminalCajaFacade terminalCajaFacade,
            VoucherFacade voucherFacade) {
        this.auxiliar = auxiliar;
        this.converter = converter;
        this.colaAlmacenes = colaAlmacenes;
        this.voucher = voucher;
        this.autorizacionFacade = autorizacionFacade;
        this.loteFacade = loteFacade;
        this.comercioFacade = comercioFacade;
        this.terminalCajaFacade = terminalCajaFacade;
        this.voucherFacade = voucherFacade;
    }

    public AutorizacionCallable(Autorizacion entity,
            AutorizacionAuxiliar auxiliar,
            ManejadorColaAlmacenes colaAlmacenes,
            String tipoTrans) {
        this.entity = entity;
        this.auxiliar = auxiliar;
        this.colaAlmacenes = colaAlmacenes;
        this.tipoTrans = tipoTrans;
    }

    @Override
    public Voucher call() throws Exception {
        Voucher voucher = new Voucher();
//        Voucher voucher = colaAlmacenes.EnviarMensajePago(entity, auxiliar.getConsumos(), tipoTrans);

        return voucher;
    }

}
