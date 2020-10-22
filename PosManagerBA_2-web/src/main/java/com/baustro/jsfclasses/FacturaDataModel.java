/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.jsfclasses;

import com.baustro.model.Autorizacion;
import com.baustro.model.Factura;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.FacturaFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class FacturaDataModel extends LazyDataModel<Factura> {

    @Inject
    private FacturaFacade ejbFacade;

    public FacturaDataModel() {
        this.setRowCount(ejbFacade.getFacturaTotalCount());
    }

    FacturaDataModel(FacturaFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
        this.setRowCount(ejbFacade.getFacturaTotalCount());
    }

    @Override
    public List<Factura> load(int first, int pageSize, String sortField,
            SortOrder sortOrder, Map<String, Object> filters) {

        List<Factura> list = ejbFacade.getFacturaList(first, pageSize, sortField, sortOrder);

        return list;
    }
}
