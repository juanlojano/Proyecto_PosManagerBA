/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.jsfclasses;

import com.baustro.model.Autorizacion;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.thread.AutorizacionCallableList;
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

public class AutorizacionDataModel extends LazyDataModel<Autorizacion> {

    @Inject
    private AutorizacionFacade ejbFacade;

    public AutorizacionDataModel() {
        this.setRowCount(ejbFacade.getAutorizacionTotalCount());
    }

    AutorizacionDataModel(AutorizacionFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
        this.setRowCount(ejbFacade.getAutorizacionTotalCount());
    }

    @Override
    public List<Autorizacion> load(int first, int pageSize, String sortField,
            SortOrder sortOrder, Map<String, Object> filters) {

        try {
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<List<Autorizacion>> resultado = executor.submit(new AutorizacionCallableList(ejbFacade,
                    first, pageSize, sortField,
                    sortOrder, filters));
            List<Autorizacion> listResp = (List<Autorizacion>) resultado.get();
            executor.shutdown();

//        List<Autorizacion> list = ejbFacade.getAutorizacionList(first, pageSize, sortField, sortOrder);
            List<Autorizacion> list = listResp;
            return list;
        } catch (InterruptedException ex) {
            Logger.getLogger(AutorizacionDataModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(AutorizacionDataModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
