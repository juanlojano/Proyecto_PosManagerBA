package com.baustro.jsfclasses;

import com.baustro.model.Comercio;
import com.baustro.jsfclasses.util.JsfUtil.PersistAction;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Institucion;
import com.baustro.sessionbean.ComercioFacade;
import com.baustro.sessionbean.InstitucionFacade;
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

@Named("institucionController")
@RequestScoped
public class InstitucionController extends AbstractController {
    
    private Institucion current;
    private List<Institucion> items = null;
    private int selectedItemIndex;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idInstitucionFlash;
    
    @EJB
    private InstitucionFacade ejbFacade;
    
    @EJB
    private TipoPagoFacade ejbTipoPagoFacade;
    
    public InstitucionController() {
    }
    
    @PostConstruct
    public void init() {
        idInstitucionFlash = (Long) flash.get("idInstitucionFlash");
        if (idInstitucionFlash != null) {
            current = ejbFacade.findInstitucionById(idInstitucionFlash);
        }
    }
    
    public Institucion getSelected() {
        if (current == null) {
            current = new Institucion();
            selectedItemIndex = -1;
        }
        
        return current;
    }
    
    public String nuevaInstitucion() {
        current = new Institucion();
        
        return "pretty:crearinstitucion";
    }
    
    public String editarInstitucion(Institucion institucion) {
        current = institucion;
        flash.put("idInstitucionFlash", current.getId());
        
        return "pretty:editarinstitucion";
    }
    
    public String verInstitucion(Institucion institucion) {
        current = institucion;
        flash.put("idInstitucionFlash", current.getId());
        
        return "pretty:verinstitucion";
    }
    
    public void eliminarInstitucion() {
        try {
            Long idInstForDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idInstitucionFacesContext");
            if (idInstForDelete != null) {
                current = ejbFacade.findInstitucionById(idInstForDelete);
                boolean existeTipoPagoConCodInst = ejbTipoPagoFacade.findTipoPagoByCodInstitucion(current.getId());
                if (existeTipoPagoConCodInst == false) {
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
            Long idE = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idInstitucionFacesContext");
            current.setId(idE);
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
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarinstitucion");
        }
    }
    
    public List<Institucion> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
            if (items != null) {
                setearVariablesFlash();
            }
        }
        
        return items;
    }
    
    public List<Institucion> obtenerItemsInstitucion() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
        }
        
        return items;
    }
    
    public Institucion getInstitucion(java.lang.Long id) {
        return ejbFacade.find(id);
    }
    
    public void onSelect(Long idInstitucion) {
//        flash.put("idInstitucionFlash", idInstitucion);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idInstitucionFacesContext", idInstitucion);
    }
    
    public List<Institucion> obtenerInstituciones() {
        List<Institucion> lista = new ArrayList<>();
        lista = ejbFacade.findAllNoNull();
        
        return lista;
    }
    
}
