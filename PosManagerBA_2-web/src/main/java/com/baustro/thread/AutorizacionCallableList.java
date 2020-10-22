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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.model.SortOrder;

/**
 *
 * @author ba0100063v
 */
public class AutorizacionCallableList implements Callable<List<Autorizacion>> {

    @Inject
    private AutorizacionFacade autorizacionFacade;

    private Autorizacion entity;
    private String tipoTrans;
    private int first;
    private int pageSize;
    private String sortField;
    private SortOrder sortOrder;
    private Map<String, Object> filters;

    ConverterUtilities cu = new ConverterUtilities();
    VoucherController vc = new VoucherController();

    public AutorizacionCallableList(AutorizacionFacade autorizacionFacade,
            int first,
            int pageSize,
            String sortField,
            SortOrder sortOrder,
            Map<String, Object> filters) {
        this.autorizacionFacade = autorizacionFacade;
        this.first = first;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
        this.filters = filters;
    }

    @Override
    public List<Autorizacion> call() throws Exception {
        List<Autorizacion> list = autorizacionFacade.getAutorizacionList(first, pageSize, sortField, sortOrder);
        return list;
    }

}
