package com.baustro.jsfclasses;

import com.baustro.model.TerminalPinPad;
import com.baustro.model.Autorizacion;
import com.baustro.model.Factura;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.FacturaFacade;
import com.baustro.sessionbean.TerminalPinPadFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

@Named("conciliacionController")
@RequestScoped
public class ConciliacionController extends AbstractController {

    private TerminalPinPad current;
    private int selectedItemIndex;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private List<TerminalPinPad> pinpadsParaCierre = new ArrayList<TerminalPinPad>();
    private Date fechaInicio;
    private Date fechaFin;
    private List<Autorizacion> listaItemsParaConciliacion = new ArrayList<>();
    private List<Factura> listaItemsParaConciliacion_Facturas = new ArrayList<>();
    private String errorConciliacionFiltro;

    @EJB
    private AutorizacionFacade ejbAutorizacionFacade;

    @EJB
    private TerminalPinPadFacade ejbFacade;

    @EJB
    private FacturaFacade ejbFacturaFacade;

    @Inject
    ComercioController comercioInject;

    @PostConstruct
    public void init() {

    }

    public ConciliacionController() {

    }

    public List<Autorizacion> obtenerItemsParaConciliacion() {

        return listaItemsParaConciliacion;
    }

    public void buscarItemsParaConciliacion() {
        listaItemsParaConciliacion = ejbAutorizacionFacade.findAutorizacionesByRangeDates(fechaInicio, fechaFin);
        RequestContext.getCurrentInstance().update("idFrmBaseLayoutMenuLeft:idFrmComercio:idDtblConciliacion");
    }

    public void buscarItemsParaConciliacion_Facturas() {
        listaItemsParaConciliacion_Facturas = ejbFacturaFacade.findFacturasByRangeDates(fechaInicio, fechaFin);
        RequestContext.getCurrentInstance().update("idFrmBaseLayoutMenuLeft:idFrmComercio:idDtblConciliacion");
    }

    public TerminalPinPad getSelected() {
        if (current == null) {
            current = new TerminalPinPad();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<TerminalPinPad> getPinpadsParaCierre() {
        return pinpadsParaCierre;
    }

    public void setPinpadsParaCierre(List<TerminalPinPad> pinpadsParaCierre) {
        this.pinpadsParaCierre = pinpadsParaCierre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Autorizacion> getListaItemsParaConciliacion() {
        return listaItemsParaConciliacion;
    }

    public void setListaItemsParaConciliacion(List<Autorizacion> listaItemsParaConciliacion) {
        this.listaItemsParaConciliacion = listaItemsParaConciliacion;
    }

    private void showMessageError() {
        RequestContext.getCurrentInstance().execute("iziToast.error({\n"
                + "                            title: 'Error',\n"
                + "                            message: 'No se pudo completar el filtro!',\n"
                + "                        });");
    }

    private void showMessageOk() {
        RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                + "                            title: 'Realizado',\n"
                + "                            message: 'Filtro realizado satisfactoriamente!',\n"
                + "                        });");
    }

    public List<Factura> getListaItemsParaConciliacion_Facturas() {
        return listaItemsParaConciliacion_Facturas;
    }

    public void setListaItemsParaConciliacion_Facturas(List<Factura> listaItemsParaConciliacion_Facturas) {
        this.listaItemsParaConciliacion_Facturas = listaItemsParaConciliacion_Facturas;
    }

}
