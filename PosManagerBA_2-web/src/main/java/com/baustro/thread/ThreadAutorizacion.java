/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.thread;

import com.baustro.model.Autorizacion;
import com.baustro.model.Factura;
import com.baustro.sessionbean.AutorizacionFacade;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 *
 * @author ba0100063v
 */
public class ThreadAutorizacion extends Thread {

    private List<Autorizacion> items;

    @EJB
    private AutorizacionFacade ejbFacade;

    @EJB
    private AutorizacionFacade ejbFacadeAutorizacion;

    private List<Autorizacion> autorizacionesByIdFactura;

    private Factura objFactura;

    public ThreadAutorizacion(List<Autorizacion> items,
            Factura objFactura,
            AutorizacionFacade ejbFacade,
            List<Autorizacion> autorizacionesByIdFactura,
            AutorizacionFacade ejbFacadeAutorizacion) {
        this.items = items;
        this.objFactura = objFactura;
        this.ejbFacade = ejbFacade;
        this.autorizacionesByIdFactura = autorizacionesByIdFactura;
        this.ejbFacadeAutorizacion = ejbFacadeAutorizacion;
    }

    @Override
    public void run() {
        try {
            System.out.println("Hilo autorización arriba");
            loadItemsAutorizacion();
        } catch (Exception ex) {
            Logger.getLogger(ThreadAutorizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadItemsAutorizacion() {
        if (items == null) {
            items = ejbFacade.findAll();
        }

        if (objFactura != null) {
            autorizacionesByIdFactura = ejbFacadeAutorizacion.findAllAutorizacionesByIdFactura(objFactura.getId());
            items = autorizacionesByIdFactura;
        }
    }

    public List<Autorizacion> getItems() {
        return items;
    }

    public void setItems(List<Autorizacion> items) {
        this.items = items;
    }

    public AutorizacionFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(AutorizacionFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public AutorizacionFacade getEjbFacadeAutorizacion() {
        return ejbFacadeAutorizacion;
    }

    public void setEjbFacadeAutorizacion(AutorizacionFacade ejbFacadeAutorizacion) {
        this.ejbFacadeAutorizacion = ejbFacadeAutorizacion;
    }

    public List<Autorizacion> getAutorizacionesByIdFactura() {
        return autorizacionesByIdFactura;
    }

    public void setAutorizacionesByIdFactura(List<Autorizacion> autorizacionesByIdFactura) {
        this.autorizacionesByIdFactura = autorizacionesByIdFactura;
    }

    public Factura getObjFactura() {
        return objFactura;
    }

    public void setObjFactura(Factura objFactura) {
        this.objFactura = objFactura;
    }

}
