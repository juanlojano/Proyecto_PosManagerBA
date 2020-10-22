/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.jsfclasses;

import com.baustro.model.Comercio;
import com.baustro.sessionbean.ComercioFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class ComercioDataModel extends LazyDataModel<Comercio> {

    @Inject
    private ComercioFacade ejbFacade;

    public ComercioDataModel() {
        this.setRowCount(ejbFacade.getComercioTotalCount());
    }

    ComercioDataModel(ComercioFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
        this.setRowCount(ejbFacade.getComercioTotalCount());
    }

    @Override
    public List<Comercio> load(int first, int pageSize, String sortField,
            SortOrder sortOrder, Map<String, Object> filters) {

        List<Comercio> list = ejbFacade.getComercioList(first, pageSize, sortField, sortOrder);
        return list;
    }
}
