/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.thread;

import com.baustro.jsfclasses.VoucherController;
import com.baustro.model.Autorizacion;
import com.baustro.model.Lote;
import com.baustro.sessionbean.LoteFacade;
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
public class LoteCallableList implements Callable<List<Lote>> {

    @Inject
    private LoteFacade loteFacade;

    private Autorizacion entity;
    private String tipoTrans;
    private int first;
    private int pageSize;
    private String sortField;
    private SortOrder sortOrder;
    private Map<String, Object> filters;

    ConverterUtilities cu = new ConverterUtilities();
    VoucherController vc = new VoucherController();

    public LoteCallableList(LoteFacade loteFacade,
            int first,
            int pageSize,
            String sortField,
            SortOrder sortOrder,
            Map<String, Object> filters) {
        this.loteFacade = loteFacade;
        this.first = first;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
        this.filters = filters;
    }

    @Override
    public List<Lote> call() throws Exception {
        List<Lote> list = loteFacade.getLoteList(first, pageSize, sortField, sortOrder);
        return list;
    }

}
