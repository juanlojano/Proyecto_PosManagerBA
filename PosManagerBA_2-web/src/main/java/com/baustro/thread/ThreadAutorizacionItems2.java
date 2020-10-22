/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.thread;

import com.baustro.model.Autorizacion;
import com.baustro.model.Factura;
import com.baustro.sessionbean.AutorizacionFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 *
 * @author ba0100063v
 */
public class ThreadAutorizacionItems2 extends Thread {

    private List<Autorizacion> items2;

    @EJB
    private AutorizacionFacade ejbFacadeAutorizacion;

    private Factura objFactura;

    public ThreadAutorizacionItems2(Factura objFactura, AutorizacionFacade ejbFacadeAutorizacion) {
        this.objFactura = objFactura;
        this.ejbFacadeAutorizacion = ejbFacadeAutorizacion;
    }

    @Override
    public void run() {
        try {
            System.out.println("Hilo autorización arriba 2");
            items2 = ejbFacadeAutorizacion.findAllAutorizacionesByIdFactura(objFactura.getId());
        } catch (Exception ex) {
            Logger.getLogger(ThreadAutorizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Autorizacion> getItems2() {
        return items2;
    }

    public void setItems2(List<Autorizacion> items2) {
        this.items2 = items2;
    }

    public AutorizacionFacade getEjbFacadeAutorizacion() {
        return ejbFacadeAutorizacion;
    }

    public void setEjbFacadeAutorizacion(AutorizacionFacade ejbFacadeAutorizacion) {
        this.ejbFacadeAutorizacion = ejbFacadeAutorizacion;
    }

    public Factura getObjFactura() {
        return objFactura;
    }

    public void setObjFactura(Factura objFactura) {
        this.objFactura = objFactura;
    }

}
