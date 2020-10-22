/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.jsfclasses;

import com.baustro.model.Lote;
import com.baustro.model.TerminalCaja;
import com.baustro.sessionbean.LoteFacade;
import com.baustro.sessionbean.TerminalCajaFacade;
import com.baustro.thread.LoteCallableList;
import com.baustro.thread.TerminalCajaCallableList;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

public class TerminalCajaDataModel extends LazyDataModel<TerminalCaja> {

    @Inject
    private TerminalCajaFacade ejbFacade;

    public TerminalCajaDataModel() {
        this.setRowCount(ejbFacade.getTerminalCajaTotalCount());
    }

    TerminalCajaDataModel(TerminalCajaFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
        this.setRowCount(ejbFacade.getTerminalCajaTotalCount());
    }

    @Override
    public List<TerminalCaja> load(int first, int pageSize, String sortField,
            SortOrder sortOrder, Map<String, Object> filters) {

        try {
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<List<TerminalCaja>> resultado = executor.submit(new TerminalCajaCallableList(ejbFacade,
                    first, pageSize, sortField,
                    sortOrder, filters));
            List<TerminalCaja> listResp = (List<TerminalCaja>) resultado.get();
            executor.shutdown();

//        List<Lote> list = ejbFacade.getLoteList(first, pageSize, sortField, sortOrder);
            List<TerminalCaja> list = listResp;
            return list;
        } catch (InterruptedException ex) {
            Logger.getLogger(TerminalCajaDataModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(TerminalCajaDataModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
