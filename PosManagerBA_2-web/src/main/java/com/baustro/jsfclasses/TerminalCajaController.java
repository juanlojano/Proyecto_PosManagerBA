package com.baustro.jsfclasses;

import com.baustro.classes.CustomException;
import com.baustro.model.TerminalCaja;
import com.baustro.jsfclasses.util.JsfUtil;
import com.baustro.model.Comercio;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Lote;
import com.baustro.model.TerminalPinPad;
import com.baustro.sessionbean.TerminalCajaFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@Named("terminalCajaController")
@RequestScoped
public class TerminalCajaController extends AbstractController {

    private List<TerminalCaja> items = null;
    private TerminalCaja current;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idTerminalCajaFlash;
    private TerminalCaja currentSelected = new TerminalCaja();
    private Long idComercioTemp;
    private int selectedItemIndex;
    private Long idPinPadTemp;
    private TerminalCajaDataModel dataModel;

    @EJB
    private TerminalCajaFacade ejbFacade;

    @Inject
    ComercioController comercioInject;

    @Inject
    TerminalPinPadController pinpadInject;

    public TerminalCajaController() {
    }

    @PostConstruct
    public void init() {
        dataModel = new TerminalCajaDataModel(ejbFacade);
        idTerminalCajaFlash = (Long) flash.get("idTerminalCajaFlash");
        if (idTerminalCajaFlash != null) {
            current = ejbFacade.findTerminalCajaById(idTerminalCajaFlash);
        }
    }

    public TerminalCaja getSelected() {
        if (current == null) {
            current = new TerminalCaja();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TerminalCajaFacade getFacade() {
        return ejbFacade;
    }

    public String nuevaCaja() {
        current = new TerminalCaja();

        return "pretty:crearcaja";
    }

    public String editarTerminalC(TerminalCaja terminalC) {
        current = terminalC;
        currentSelected = terminalC;
        flash.put("idTerminalCajaFlash", current.getId());

        return "pretty:editarcaja";
    }

    public String verTerminalC(TerminalCaja terminalC) {
        current = terminalC;
        currentSelected = terminalC;
        flash.put("idTerminalCajaFlash", current.getId());

        return "pretty:vercaja";
    }

    public void eliminarTerminalCaja() {
        try {
            Long idTerCajaForDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idTerminalCajaFacesContext");
            if (idTerCajaForDelete != null) {
                current = ejbFacade.findTerminalCajaById(idTerCajaForDelete);
                current.setEstado(EstadoEntity.ELIMINADA);
                destroy();
            }
        } catch (Exception e) {
            redireccionarPaginaError(e);
        }
    }

    public void create() {
        try {
            current.setEstado(EstadoEntity.CREADA);
            persist(JsfUtil.PersistAction.CREATE);
            if (!JsfUtil.isValidationFailed()) {
                items = null;
            }
        } catch (CustomException ex) {
            redireccionarPaginaError(ex);
        }
    }

    public void update() {
        try {
            current.setEstado(EstadoEntity.MODIFICADA);
            persist(JsfUtil.PersistAction.UPDATE);
        } catch (CustomException ex) {
            redireccionarPaginaError(ex);
        }
    }

    public void destroy() {
        try {
            persist(JsfUtil.PersistAction.DELETE);
        } catch (CustomException ex) {
            redireccionarPaginaError(ex);
        }
    }

    private void persist(JsfUtil.PersistAction persistAction) throws CustomException {
        if (current != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == JsfUtil.PersistAction.CREATE) {
                    getFacade().create(current);
                    flash.put("persist", "persist");
                }
                if (persistAction == JsfUtil.PersistAction.UPDATE) {
                    getFacade().edit(current);
                    flash.put("updated", "updated");
                }
                if (persistAction == JsfUtil.PersistAction.DELETE) {
                    getFacade().edit(current);
                    flash.put("deleted", "deleted");
                }
            } catch (Exception ex) {
                flash.put("noDeleted", "noDeleted");
                redireccionarPaginaError(ex);
            }
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarcaja");
        }
    }

    public void onSelect(Long idTerminalCaja) {
        flash.put("idTerminalCajaFlash", idTerminalCaja);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idTerminalCajaFacesContext", idTerminalCaja);
    }

    public List<TerminalCaja> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
            if (items != null) {
                setearVariablesFlash();
            }
        }

        return items;
    }

    public TerminalCaja getTerminalCaja(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public List<Comercio> obtenerComercios() {
        List<Comercio> lista = new ArrayList<>();
        for (Comercio comercioItem : comercioInject.obtenerComercios()) {
            lista.add(comercioItem);
        }

        return lista;
    }

    public List<TerminalPinPad> obtenerPinPads() {
        List<TerminalPinPad> terminalPP = new ArrayList<>();
        for (TerminalPinPad pinpadItem : pinpadInject.obtenerPinPads()) {
            terminalPP.add(pinpadItem);
        }

        return terminalPP;
    }

    public Long getIdComercioTemp() {
        return idComercioTemp;
    }

    public void setIdComercioTemp(Long idComercioTemp) {
        this.idComercioTemp = idComercioTemp;
    }

    public Long getIdPinPadTemp() {
        return idPinPadTemp;
    }

    public void setIdPinPadTemp(Long idPinPadTemp) {
        this.idPinPadTemp = idPinPadTemp;
    }

    public LazyDataModel<TerminalCaja> getModel() {

        return dataModel;
    }

}
