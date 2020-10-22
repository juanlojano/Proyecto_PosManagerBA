package com.baustro.jsfclasses;

import com.baustro.model.Comercio;
import com.baustro.jsfclasses.util.JsfUtil.PersistAction;
import com.baustro.model.EstadoEntity;
import com.baustro.model.Institucion;
import com.baustro.model.Plazo;
import com.baustro.model.TipoDiferido;
import com.baustro.model.TipoPago;
import com.baustro.sessionbean.BinTarjetaFacade;
import com.baustro.sessionbean.ComercioFacade;
import com.baustro.sessionbean.InstitucionFacade;
import com.baustro.sessionbean.TipoPagoFacade;
import com.baustro.sessionbean.TerminalPinPadFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

@Named("tipoPagoController")
@RequestScoped
public class TipoPagoController extends AbstractController {

    private TipoPago current;
    private List<TipoPago> items = null;
    private int selectedItemIndex;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idTipoPagoFlash;

    @EJB
    private TipoPagoFacade ejbFacade;

    @EJB
    private BinTarjetaFacade ejbBinTarjetaFacade;

    @Inject
    InstitucionController institucionInject;

    @Inject
    PlazoController plazoInject;

    @Inject
    TipoDiferidoController tipoDiferidoInject;

    public TipoPagoController() {
    }

    @PostConstruct
    public void init() {
        idTipoPagoFlash = (Long) flash.get("idTipoPagoFlash");
        if (idTipoPagoFlash != null) {
            current = ejbFacade.findTipoPagoById(idTipoPagoFlash);
        }
    }

    public TipoPago getSelected() {
        if (current == null) {
            current = new TipoPago();
            selectedItemIndex = -1;
        }

        return current;
    }

    public String nuevoTipoPago() {
        current = new TipoPago();

        return "pretty:creartipopago";
    }

    public String editarTipoPago(TipoPago tipoPago) {
        current = tipoPago;
        flash.put("idTipoPagoFlash", current.getId());

        return "pretty:editartipopago";
    }

    public String verTipoPago(TipoPago tipoPago) {
        current = tipoPago;
        flash.put("idTipoPagoFlash", current.getId());

        return "pretty:vertipopago";
    }

    public void eliminarTipoPago() {
        try {
            Long idTipoPagoForDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idTipoPagoFacesContext");
            if (idTipoPagoForDelete != null) {
                current = ejbFacade.findTipoPagoById(idTipoPagoForDelete);
                boolean existeBinTarjConCodTipPag = ejbBinTarjetaFacade.findBinTarjByCodCodTipPag(current.getId());
                if (existeBinTarjConCodTipPag == false) {
                    current.setEstado(EstadoEntity.ELIMINADA);
                    destroy();
                } else {
                    flash.put("noDeleted", "noDeleted");
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listartipopago");
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
        Long idE = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idTipoPagoFacesContext");
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
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listartipopago");
        }
    }

    public List<TipoPago> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
            if (items != null) {
                setearVariablesFlash();
            }
        }

        return items;
    }

    public List<TipoPago> obtenerItemsTipoPago() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
        }

        return items;
    }

    public List<Institucion> obtenerInstituciones() {
        List<Institucion> lista = new ArrayList<>();
        for (Institucion institucionItem : institucionInject.obtenerInstituciones()) {
            lista.add(institucionItem);
        }

        return lista;
    }

    public List<Plazo> obtenerPlazos() {
        List<Plazo> lista = new ArrayList<>();
        for (Plazo plazoItem : plazoInject.obtenerPlazos()) {
            lista.add(plazoItem);
        }

        return lista;
    }

    public List<TipoDiferido> obtenerTiposDiferido() {
        List<TipoDiferido> lista = new ArrayList<>();
        for (TipoDiferido tipoDiferidoItem : tipoDiferidoInject.obtenerTipoDiferidos()) {
            lista.add(tipoDiferidoItem);
        }

        return lista;
    }

    public TipoPago getTipoPago(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public void onSelect(Long idTipoPago) {
        flash.put("idTipoPagoFlash", idTipoPago);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idTipoPagoFacesContext", idTipoPago);
    }

    public List<TipoPago> obtenerTiposPago() {
        List<TipoPago> lista = new ArrayList<>();
        lista = ejbFacade.findAllNoNull();

        return lista;
    }

}
