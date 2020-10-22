package com.baustro.jsfclasses;

import com.baustro.jsfclasses.util.JsfUtil.PersistAction;
import com.baustro.model.EstadoEntity;
import com.baustro.model.BinTarjeta;
import com.baustro.model.TipoPago;
import com.baustro.sessionbean.BinTarjetaFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

@Named("binTarjetaController")
@RequestScoped
public class BinTarjetaController extends AbstractController {

    private BinTarjeta current;
    private List<BinTarjeta> items = null;
    private int selectedItemIndex;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idBinTarjetaFlash;

    @EJB
    private BinTarjetaFacade ejbFacade;

    @Inject
    TipoPagoController tipoPagoInject;

    public BinTarjetaController() {
    }

    @PostConstruct
    public void init() {
        idBinTarjetaFlash = (Long) flash.get("idBinTarjetaFlash");
        if (idBinTarjetaFlash != null) {
            current = ejbFacade.findBinTarjetaById(idBinTarjetaFlash);
        }
    }

    public BinTarjeta getSelected() {
        if (current == null) {
            current = new BinTarjeta();
            selectedItemIndex = -1;
        }

        return current;
    }

    public String nuevoBinTarjeta() {
        current = new BinTarjeta();

        return "pretty:crearbin";
    }

    public String editarBinTarjeta(BinTarjeta bin) {
        current = bin;
        flash.put("idBinTarjetaFlash", current.getId());

        return "pretty:editarbin";
    }

    public String verBinTarjeta(BinTarjeta bin) {
        current = bin;
        flash.put("idBinTarjetaFlash", current.getId());

        return "pretty:verbin";
    }

    public void eliminarBinTarjeta() {
        try {
            Long idBinTarjetaForDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idBinTarjetaFacesContext");
            if (idBinTarjetaForDelete != null) {
                current = ejbFacade.findBinTarjetaById(idBinTarjetaForDelete);
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
            persist(PersistAction.CREATE);
        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        }
    }

    public void update() {
        Long idE = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idBinTarjetaFacesContext");
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
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarbin");
        }
    }

    public List<BinTarjeta> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
            if (items != null) {
                setearVariablesFlash();
            }
        }

        return items;
    }

    public List<BinTarjeta> obtenerItemsBinTarjeta() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
        }

        return items;
    }

    public List<TipoPago> obtenerTiposPago() {
        List<TipoPago> lista = new ArrayList<>();
        for (TipoPago tipoPagoItem : tipoPagoInject.obtenerTiposPago()) {
            lista.add(tipoPagoItem);
        }

        return lista;
    }

    public BinTarjeta getBinTarjeta(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public void onSelect(Long idBinTarjeta) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idBinTarjetaFacesContext", idBinTarjeta);
    }

    public List<BinTarjeta> obtenerBinTarjetas() {
        List<BinTarjeta> lista = new ArrayList<>();
        lista = ejbFacade.findAllNoNull();

        return lista;
    }

    public BinTarjeta getCurrent() {
        return current;
    }

    public void setCurrent(BinTarjeta current) {
        this.current = current;
    }

}
