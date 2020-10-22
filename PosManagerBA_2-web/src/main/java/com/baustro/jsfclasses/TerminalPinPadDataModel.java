/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.jsfclasses;

import com.baustro.model.TerminalPinPad;
import com.baustro.sessionbean.TerminalPinPadFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class TerminalPinPadDataModel extends LazyDataModel<TerminalPinPad> {

    @Inject
    private TerminalPinPadFacade ejbFacade;

    public TerminalPinPadDataModel() {
        this.setRowCount(ejbFacade.getTerminalPinPadTotalCount());
    }

    TerminalPinPadDataModel(TerminalPinPadFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
        this.setRowCount(ejbFacade.getTerminalPinPadTotalCount());
    }

    @Override
    public List<TerminalPinPad> load(int first, int pageSize, String sortField,
            SortOrder sortOrder, Map<String, Object> filters) {

        List<TerminalPinPad> list = ejbFacade.getTerminalPinPadList(first, pageSize, sortField, sortOrder);
        return list;
    }
}
