/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.jsfclasses;

import com.baustro.jsfclasses.util.JsfUtil;
import com.baustro.jsfclasses.util.PaginationHelper;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Voucher;
import com.baustro.sessionbean.VoucherFacade;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author ue01000632
 */
@Named("voucherController")
@RequestScoped
public class VoucherController implements Serializable {

    private Voucher current;
    @EJB
    private VoucherFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Long idComercioTemp = 0L;

//    public void create(VoucherFacade voucherFacade, Voucher voucher) {
    public void create(Voucher voucher) {
//        ejbFacade=voucherFacade;
        voucher.setEstado(EstadoEntity.CREADA);
//        Voucher curr = new Voucher();
//        curr = voucher;
        current = voucher;
        ejbFacade.create(current);
    }

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VoucherUpdated"));

    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VoucherDeleted"));
    }

    protected void setEmbeddableKeys() {
    }

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (current != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    getEjbFacade().edit(current);
                } else {
                    getEjbFacade().remove(current);

                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, "ERROR_1");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "ERROR_2");
            }
        }
    }

    public VoucherFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(VoucherFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

}
