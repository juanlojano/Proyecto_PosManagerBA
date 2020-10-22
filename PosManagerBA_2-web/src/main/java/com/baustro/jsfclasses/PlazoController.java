package com.baustro.jsfclasses;

import com.baustro.model.Comercio;
import com.baustro.jsfclasses.util.JsfUtil.PersistAction;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Institucion;
import com.baustro.model.Plazo;
import com.baustro.sessionbean.ComercioFacade;
import com.baustro.sessionbean.InstitucionFacade;
import com.baustro.sessionbean.PlazoFacade;
import com.baustro.sessionbean.TerminalPinPadFacade;
import com.baustro.sessionbean.TipoPagoFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

@Named("plazoController")
@RequestScoped
public class PlazoController extends AbstractController {

    private Plazo current;
    private List<Plazo> items = null;
    private int selectedItemIndex;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idPlazoFlash;

    @EJB
    private PlazoFacade ejbFacade;

    @EJB
    private TipoPagoFacade ejbTipoPagoFacade;

    public PlazoController() {
    }

    @PostConstruct
    public void init() {
        idPlazoFlash = (Long) flash.get("idPlazoFlash");
        if (idPlazoFlash != null) {
            current = ejbFacade.findPlazoById(idPlazoFlash);
        }
    }

    public Plazo getSelected() {
        if (current == null) {
            current = new Plazo();
            selectedItemIndex = -1;
        }

        return current;
    }

    public String nuevoPlazo() {
        current = new Plazo();

        return "pretty:crearplazo";
    }

    public String editarPlazo(Plazo plazo) {
        current = plazo;
        flash.put("idPlazoFlash", current.getId());

        return "pretty:editarplazo";
    }

    public String verPlazo(Plazo plazo) {
        current = plazo;
        flash.put("idPlazoFlash", current.getId());

        return "pretty:verplazo";
    }

    public void eliminarPlazo() {
        try {
            Long idPlazoForDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idPlazoFacesContext");
            if (idPlazoForDelete != null) {
                current = ejbFacade.findPlazoById(idPlazoForDelete);
                boolean existeTipoPagoConCodPlazo = ejbTipoPagoFacade.findTipoPagoByCodPlazo(current.getId());
                if (existeTipoPagoConCodPlazo == false) {
                    current.setEstado(EstadoEntity.ELIMINADA);
                    destroy();
                } else {
                    flash.put("noDeleted", "noDeleted");
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarplazo");
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
        Long idE = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idPlazoFacesContext");
        current.setId(idE);
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
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarplazo");
        }
    }

    public List<Plazo> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
            if (items != null) {
                setearVariablesFlash();
            }
        }

        return items;
    }

    public List<Plazo> obtenerItemsPlazo() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
        }

        return items;
    }

    public Plazo getInstitucion(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public void onSelect(Long idPlazo) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idPlazoFacesContext", idPlazo);
    }

    public List<Plazo> obtenerPlazos() {
        List<Plazo> lista = new ArrayList<>();
        lista = ejbFacade.findAllNoNull();

        return lista;
    }

}
