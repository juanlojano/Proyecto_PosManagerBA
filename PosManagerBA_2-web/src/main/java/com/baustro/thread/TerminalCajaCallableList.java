/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.thread;

import com.baustro.jsfclasses.VoucherController;
import com.baustro.model.Autorizacion;
import com.baustro.model.Lote;
import com.baustro.model.TerminalCaja;
import com.baustro.sessionbean.LoteFacade;
import com.baustro.sessionbean.TerminalCajaFacade;
import com.baustro.utility.ConverterUtilities;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import org.primefaces.model.SortOrder;

/**
 *
 * @author ba0100063v
 */
public class TerminalCajaCallableList implements Callable<List<TerminalCaja>> {

    @Inject
    private TerminalCajaFacade terminalCajaFacade;

    private Autorizacion entity;
    private String tipoTrans;
    private int first;
    private int pageSize;
    private String sortField;
    private SortOrder sortOrder;
    private Map<String, Object> filters;

    ConverterUtilities cu = new ConverterUtilities();
    VoucherController vc = new VoucherController();

    public TerminalCajaCallableList(TerminalCajaFacade terminalCajaFacade,
            int first,
            int pageSize,
            String sortField,
            SortOrder sortOrder,
            Map<String, Object> filters) {
        this.terminalCajaFacade = terminalCajaFacade;
        this.first = first;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
        this.filters = filters;
    }

    @Override
    public List<TerminalCaja> call() throws Exception {
        List<TerminalCaja> list = terminalCajaFacade.getTerminalCajaList(first, pageSize, sortField, sortOrder);

        return list;
    }

}
