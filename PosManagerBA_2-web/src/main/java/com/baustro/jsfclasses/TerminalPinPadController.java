package com.baustro.jsfclasses;

import com.baustro.model.TerminalPinPad;
import com.baustro.jsfclasses.util.JsfUtil;
import com.baustro.jsfclasses.util.JsfUtil.PersistAction;
import com.baustro.mensajeria.jms.MensajeProducer;
import com.baustro.model.Comercio;
import com.baustro.model.EstadoEntity;
import com.baustro.model.EstadoLote;
import com.baustro.model.Lote;
import com.baustro.sessionbean.LoteFacade;
import com.baustro.sessionbean.TerminalCajaFacade;
import com.baustro.sessionbean.TerminalPinPadFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@Named("terminalPinPadController")
@RequestScoped
public class TerminalPinPadController extends AbstractController {

    private TerminalPinPad current;
    private List<TerminalPinPad> items = null;
    private int selectedItemIndex;
    private Comercio comercioSelected;
    private Long idComercioTemp;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idPinPadFlash;
    private List<TerminalPinPad> pinpadsParaCierre = new ArrayList<TerminalPinPad>();
    private TerminalPinPadDataModel dataModel;

    @EJB
    private TerminalPinPadFacade ejbFacade;

    @EJB
    private TerminalCajaFacade ejbTerminalCajaFacade;

    @Inject
    ComercioController comercioInject;

    @Inject
    LoteController loteController;

    @Inject
    private MensajeProducer mensajeProducer;

    @EJB
    private LoteFacade ejbLoteFacade;

    @PostConstruct
    public void init() {
        System.out.println("--------------POSTCONST0----TP: " + ejbFacade);
        dataModel = new TerminalPinPadDataModel(ejbFacade);

        idPinPadFlash = (Long) flash.get("idPinPadFlash");

        if (idPinPadFlash != null) {
            current = ejbFacade.findTerminalPinPadById(idPinPadFlash);
        }
    }

    public TerminalPinPadController() {
    }

    public List<TerminalPinPad> getPinpadsParaCierre() {
        return pinpadsParaCierre;
    }

    public void setPinpadsParaCierre(List<TerminalPinPad> pinpadsParaCierre) {
        this.pinpadsParaCierre = pinpadsParaCierre;
    }

    public void onSelect(Long idPinPad) {
        flash.put("idPinPadFlash", idPinPad);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idTerminalPinPadFacesContext", idPinPad);
    }

    public String nuevo() {
        current = new TerminalPinPad();

        return "pretty:crearpinpad";
    }

    public String editarTerminalPP(TerminalPinPad terminalPP) {
        current = terminalPP;
        flash.put("idPinPadFlash", current.getId());

        return "pretty:editarpinpad";
    }

    public String verTerminalPP(TerminalPinPad terminalPP) {
        current = terminalPP;
        flash.put("idPinPadFlash", current.getId());

        return "pretty:verpinpad";
    }

    public void create() {
        try {
            current.setEstado(EstadoEntity.CREADA);
            List<Lote> lotes = new ArrayList<>(); 
            lotes.add(crearLoteParaPinpad(current));
            current.setLote(lotes);
            persist(PersistAction.CREATE);
        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        } 
    }

    private Lote crearLoteParaPinpad(TerminalPinPad current) {
        Lote lote = new Lote();
//        lote.setNumeroLote(obtenerNumeroLote());
        lote.setNumeroLote(0);
        lote.setValor(BigDecimal.ZERO);
        lote.setEstado(EstadoEntity.CREADA);
        lote.setEstadoLote(EstadoLote.ABIERTO);
        lote.setFechaLote(new Date());
        lote.setPinpad(current);

        return lote;
    }

    public void update() {
        try {
            current.setEstado(EstadoEntity.MODIFICADA);
            persist(PersistAction.UPDATE);
        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        }
    }

    public void eliminarTerminalPinPad() {
        try {
            Long idTerminalPinPadForDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idTerminalPinPadFacesContext");
            if (idTerminalPinPadForDelete != null) {
                current = ejbFacade.findTerminalPinPadById(idTerminalPinPadForDelete);
                boolean existeCajaConIdPinPad = ejbTerminalCajaFacade.findCajaByIdPinPad(current.getId());
                if (existeCajaConIdPinPad == false) {
                    current.setEstado(EstadoEntity.ELIMINADA);
                    destroy();
                } else {
                    flash.put("noDeleted", "noDeleted");
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarpinpad");
                }
            }
        } catch (Exception e) {
            redireccionarPaginaError(e);
        }

        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarpinpad");
    }

    private void persist(JsfUtil.PersistAction persistAction) {
        if (current != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(current);
                    flash.put("persist", "persist");
                }
                if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(current);
                    flash.put("updated", "updated");
                }
                if (persistAction == PersistAction.DELETE) {
                    getFacade().edit(current);
                    eliminarLotePinpad(current);
                    flash.put("deleted", "deleted");
                }
            } catch (Exception ex) {
                redireccionarPaginaError(ex);
            }
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarpinpad");
        }
    }

    public void destroy() {
        try {
            persist(PersistAction.DELETE);
        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        }
    }

    private void eliminarLotePinpad(TerminalPinPad current) {
        Lote lote = ejbLoteFacade.findLoteByIdPinpad(current.getId());
        if (lote != null) {
            lote.setEstado(EstadoEntity.ELIMINADA);
            ejbLoteFacade.edit(lote);
        }
    }

    public List<TerminalPinPad> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
            if (items != null) {
                setearVariablesFlash();
            }
        }

        return items;
    }

    public List<Comercio> obtenerComercios() {
        List<Comercio> lista = new ArrayList<>();
        for (Comercio comercioItem : comercioInject.obtenerComercios()) {
            lista.add(comercioItem);
        }

        return lista;
    }

    public TerminalPinPad getTerminalPinPad(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public List<TerminalPinPad> obtenerPinPads() {
        List<TerminalPinPad> lista = new ArrayList<>();
        lista = ejbFacade.findAllNoNull();

        return lista;
    }

    public LazyDataModel<TerminalPinPad> getModel() {

        return dataModel;
    }

    public Long getIdComercioTemp() {
        return idComercioTemp;
    }

    public void setIdComercioTemp(Long idComercioTemp) {
        this.idComercioTemp = idComercioTemp;
    }

    public Comercio getComercioSelected() {
        return comercioSelected;
    }

    public void setComercioSelected(Comercio comercioSelected) {
        this.comercioSelected = comercioSelected;
    }

    public TerminalPinPad getSelected() {
        if (current == null) {
            current = new TerminalPinPad();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TerminalPinPadFacade getFacade() {
        return ejbFacade;
    }

    public void cerrarPinpads() throws JMSException {
        try {
            mensajeProducer.consumerString(pinpadsParaCierre);
            System.out.println("Mensaje cerrar pp");
            RequestContext.getCurrentInstance().execute("iziToast.Warning({\n"
                    + "                            title: 'CERRADO',\n"
                    + "                            message: 'CERRADO!',\n"
                    + "                        });");

            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            } catch (IOException ex) {
                Logger.getLogger(MensajeProducer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (NamingException ex) {
            Logger.getLogger(TerminalPinPadController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public TerminalPinPadDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(TerminalPinPadDataModel dataModel) {
        this.dataModel = dataModel;
    }

//    private Integer obtenerNumeroLote() {
//        List<TerminalPinPad> listaPinpads = ejbFacade.findAll();
//
//        return (listaPinpads.size() * 1000) + 1;
//    }
}
