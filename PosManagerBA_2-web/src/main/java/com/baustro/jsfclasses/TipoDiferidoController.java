package com.baustro.jsfclasses;

import com.baustro.jsfclasses.util.JsfUtil.PersistAction;
import com.baustro.model.EstadoEntity;
import com.baustro.model.TipoDiferido;
import com.baustro.sessionbean.TipoDiferidoFacade;
import com.baustro.sessionbean.TipoPagoFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

@Named("tipoDiferidoController")
@RequestScoped
public class TipoDiferidoController extends AbstractController {

    private TipoDiferido current;
    private List<TipoDiferido> items = null;
    private int selectedItemIndex;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idTipoDiferidoFlash;

    @EJB
    private TipoDiferidoFacade ejbFacade;

    @EJB
    private TipoPagoFacade ejbTipoPagoFacade;

    public TipoDiferidoController() {
    }

    @PostConstruct
    public void init() {
        idTipoDiferidoFlash = (Long) flash.get("idTipoDiferidoFlash");
        if (idTipoDiferidoFlash != null) {
            current = ejbFacade.findTipoDiferidoById(idTipoDiferidoFlash);
        }
    }

    public TipoDiferido getSelected() {
        if (current == null) {
            current = new TipoDiferido();
            selectedItemIndex = -1;
        }

        return current;
    }

    public String nuevoTipoDiferido() {
        current = new TipoDiferido();

        return "pretty:creartipodiferido";
    }

    public String editarTipoDiferido(TipoDiferido tipoDiferido) {
        current = tipoDiferido;
        flash.put("idTipoDiferidoFlash", current.getId());

        return "pretty:editartipodiferido";
    }

    public String verTipoDiferido(TipoDiferido plazo) {
        current = plazo;
        flash.put("idTipoDiferidoFlash", current.getId());

        return "pretty:vertipodiferido";
    }

    public void eliminarTipoDiferido() {
        try {
            Long idTipoDiferidoForDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idTipoDiferidoFacesContext");
            if (idTipoDiferidoForDelete != null) {
                current = ejbFacade.findTipoDiferidoById(idTipoDiferidoForDelete);
                boolean existeTipoPagoConCodTipoDiferido = ejbTipoPagoFacade.findTipoPagoByCodTipoDiferido(current.getId());
                if (existeTipoPagoConCodTipoDiferido == false) {
                    current.setEstado(EstadoEntity.ELIMINADA);
                    destroy();
                } else {
                    flash.put("noDeleted", "noDeleted");
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listartipodiferido");
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
        Long idE = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idTipoDiferidoFacesContext");
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
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listartipodiferido");
        }
    }

    public List<TipoDiferido> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
            if (items != null) {
                setearVariablesFlash();
            }
        }

        return items;
    }

    public List<TipoDiferido> obtenerItemsTipoDiferido() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
        }

        return items;
    }

    public TipoDiferido getTipoDiferido(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public void onSelect(Long idTipoDiferido) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idTipoDiferidoFacesContext", idTipoDiferido);
    }

    public List<TipoDiferido> obtenerTipoDiferidos() {
        List<TipoDiferido> lista = new ArrayList<>();
        lista = ejbFacade.findAllNoNull();

        return lista;
    }

}
