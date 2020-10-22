package com.baustro.jsfclasses;

import com.baustro.model.Comercio;
import com.baustro.jsfclasses.util.JsfUtil.PersistAction;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Factura;
import com.baustro.sessionbean.ComercioFacade;
import com.baustro.sessionbean.TerminalPinPadFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import org.primefaces.model.LazyDataModel;

@Named("comercioController")
@RequestScoped
public class ComercioController extends AbstractController {

    private Comercio current;
    private List<Comercio> items = null;
    private int selectedItemIndex;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idComercioFlash;
    private ComercioDataModel dataModel;

    @EJB
    private ComercioFacade ejbFacade;

    @EJB
    private TerminalPinPadFacade ejbTerminalPinPadFacade;

    public ComercioController() {
    }

    @PostConstruct
    public void init() {
        dataModel = new ComercioDataModel(ejbFacade);
        idComercioFlash = (Long) flash.get("idComercioFlash");
        if (idComercioFlash != null) {
            current = ejbFacade.findComercioById(idComercioFlash);
        }
    }

    public Comercio getSelected() {
        if (current == null) {
            current = new Comercio();
            selectedItemIndex = -1;
        }

        return current;
    }

    public String nuevoComercio() {
        current = new Comercio();

        return "pretty:crearcomercio";
    }

    public String editarComercio(Comercio comercio) {
        current = comercio;
        flash.put("idComercioFlash", current.getId());

        return "pretty:editarcomercio";
    }

    public String verComercio(Comercio comercio) {
        current = comercio;
        flash.put("idComercioFlash", current.getId());

        return "pretty:vercomercio";
    }

    public void eliminarComercio() {
        try {
            Long idComForDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idComercioFacesContext");
            if (idComForDelete != null) {
                current = ejbFacade.findComercioById(idComForDelete);
                boolean existePinpadConCodComercio = ejbTerminalPinPadFacade.findTerminalPinPadByCodComercio(current.getId());
                if (existePinpadConCodComercio == false) {
                    current.setEstado(EstadoEntity.ELIMINADA);
                    destroy();
                } else {
                    flash.put("noDeleted", "noDeleted");
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarcomercio");
                }
            }
        } catch (Exception e) {
            redireccionarPaginaError(e);
        }
    }

    public void create() {
        try {
            current.setEstado(EstadoEntity.CREADA);
            persist(PersistAction.CREATE);
        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        }
    }

    public void update() {
        try {
            current.setEstado(EstadoEntity.MODIFICADA);
            persist(PersistAction.UPDATE);
        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        }
    }

    public void destroy() {
        try {
            persist(PersistAction.DELETE);
        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        }
    }

    private void persist(PersistAction persistAction) {
        if (current != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    ejbFacade.create(current);
                    flash.put("persist", "persist");
                }
                if (persistAction == PersistAction.UPDATE) {
                    ejbFacade.edit(current);
                    flash.put("updated", "updated");
                }
                if (persistAction == PersistAction.DELETE) {
                    ejbFacade.edit(current);
                    flash.put("deleted", "deleted");
                }
            } catch (Exception ex) {
                flash.put("noDeleted", "noDeleted");
                redireccionarPaginaError(ex);
            }
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarcomercio");
        }
    }

    public LazyDataModel<Comercio> getModel() {

        return dataModel;
    }

    public List<Comercio> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
            if (items != null) {
                setearVariablesFlash();
            }
        }

        return items;
    }

    public List<Comercio> obtenerItemsComercio() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
        }

        return items;
    }

    public Comercio getComercio(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public void onSelect(Long idComercio) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idComercioFacesContext", idComercio);
    }

    public List<Comercio> obtenerComercios() {
        List<Comercio> lista = new ArrayList<>();
        lista = ejbFacade.findAllNoNull();

        return lista;
    }

    public ComercioDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(ComercioDataModel dataModel) {
        this.dataModel = dataModel;
    }

}
