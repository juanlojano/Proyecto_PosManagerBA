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
public class ThreadAutorizacionItems1 extends Thread {

    @EJB
    private AutorizacionFacade ejbFacade;

    private List<Autorizacion> items1;

    public ThreadAutorizacionItems1(AutorizacionFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    @Override
    public void run() {
        try {
            System.out.println("Hilo autorización arriba 1");
            items1 = ejbFacade.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ThreadAutorizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public AutorizacionFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(AutorizacionFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public List<Autorizacion> getItems1() {
        return items1;
    }

    public void setItems1(List<Autorizacion> items1) {
        this.items1 = items1;
    }
}
