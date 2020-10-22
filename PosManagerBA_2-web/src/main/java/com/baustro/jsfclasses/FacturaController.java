package com.baustro.jsfclasses;

import com.baustro.classes.CustomException;
import com.baustro.classes.VoucherClass;
import com.baustro.interceptorbinding.LoggingInterceptorBinding;
import com.baustro.model.Autorizacion;
import com.baustro.model.Voucher;
import com.baustro.model.Factura;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.FacturaFacade;
import com.baustro.sessionbean.VoucherFacade;
import com.baustro.util.JsfUtil;
import com.baustro.util.JsfUtil.PersistAction;
import com.baustro.utility.ConverterUtilities;
import com.baustro.utility.Reporte;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

@Named("facturaController")
@ViewScoped
@LoggingInterceptorBinding
//@RequestScoped
public class FacturaController implements Serializable {

    @EJB
    private FacturaFacade ejbFacade;

    @EJB
    private AutorizacionFacade ejbFacadeAutorizacion;

    @EJB
    private VoucherFacade ejbFacadeVoucher;

    private List<Factura> items = null;
    private Factura selected;

    private Autorizacion autTemp;
    private Autorizacion autorizacionSelected;
    private List<Autorizacion> autorizacionesByIdFactura;
    private boolean existeAutorizacion = false;
    private FacturaDataModel dataModel;

    public Autorizacion getAutTemp() {
        return autTemp;
    }

    public void setAutTemp(Autorizacion autTemp) {
        this.autTemp = autTemp;
    }

    private Factura currentFactura;

    private Autorizacion currentAuth = null;

    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idFacturaFlash;

    @PostConstruct
    public void init() {
        dataModel = new FacturaDataModel(ejbFacade);
        autorizacionesByIdFactura = new ArrayList<>();
        idFacturaFlash = (Long) flash.get("idFacturaFlash");
        if (idFacturaFlash != null) {
            currentFactura = ejbFacade.FindFacturaById(idFacturaFlash);
        }
    }

    public Autorizacion getCurrentAuth() {
        return currentAuth;
    }

    public void setCurrentAuth(Autorizacion currentAuth) {
        this.currentAuth = currentAuth;
    }

    public Factura getCurrentFactura() {

        return currentFactura;
    }

    public void setCurrentFactura(Factura currentFactura) {
        this.currentFactura = currentFactura;
    }

    @Inject
    private AutorizacionController autController;

    public FacturaController() {
    }

    public Factura getSelected() {
        return selected;
    }

    public void setSelected(Factura selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FacturaFacade getFacade() {
        return ejbFacade;
    }

    public Factura prepareCreate() {
        selected = new Factura();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FacturaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FacturaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FacturaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

//    public List<Factura> obtenerItemsFactura() {
//        List<Factura> lista = ejbFacade.findAll();
//
//        return lista;
//    }
//
    public LazyDataModel<Factura> getModel() {
//        LazyDataModel<Factura> lista = ejbFacade.findAll();

        return dataModel;
    }

    public List<Factura> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Factura getFactura(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Factura> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Factura> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Factura.class)
    public static class FacturaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FacturaController controller = (FacturaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "facturaController");
            return controller.getFactura(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Factura) {
                Factura o = (Factura) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Factura.class.getName()});
                return null;
            }
        }

    }

    public String verAutorizacion(Factura factura) {
        currentFactura = factura;
        try {
            Autorizacion autorizacion = ejbFacadeAutorizacion.findLastAutorizacionByIdFactura(factura.getId());
            autController.setCurrentAutorizacion(autorizacion);

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idAutorizacionContext", autorizacion.getId());

        } catch (Exception e) {
            redireccionarPaginaError(e);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error" + e));
        }

        return "pretty:verautorizacion";
    }

    private void redireccionarPaginaError(Exception e) {
        CustomException customException = new CustomException(e);
        customException = customException.retornarCustomException();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoCustomException", customException);
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:errorPage");
//            CatalogoError catalogoError = ejbErrorFacade.FindErrorByCodigo(customException.getCodigo());
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoCatalogoError", catalogoError);
    }

    public String mostrarMensaje(Factura f) {
        try {
            Autorizacion a = ejbFacadeAutorizacion.findLastAutorizacionByIdFactura(f.getId());
            if (a == null) {
                return "No existe autorización para esta factura";

            } else {
                return "Ver autorización de factura";
            }
        } catch (Exception e) {
            redireccionarPaginaError(e);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error" + e));
            return "No existe autorización para esta factura";
        }
    }

    public String mostrarMensaje_print(Factura f) {
        try {
            Autorizacion a = ejbFacadeAutorizacion.findAutorizacionByIdFactura(f.getId());
            return "Imprimir factura";
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error" + e));
            redireccionarPaginaError(e);
            return "No existe autorización para imprimir";
        }
    }

    public boolean verificarAutorizacion(Factura f) {
        try {
            Autorizacion a = ejbFacadeAutorizacion.findLastAutorizacionByIdFacturaAndEstado(f.getId());
            if (a == null) {
                return true;

            } else {
                return false;
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error" + e));
            redireccionarPaginaError(e);
            return true;
        }
    }

    public boolean verificarAutorizacion() {
        try {
            if (currentAuth != null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error" + e));
            redireccionarPaginaError(e);
            return true;
        }
    }

    public String verFactura(Factura factura) {
        currentFactura = factura;
        flash.put("idFacturaFlash", currentFactura.getId());

//        try {
//            if (ejbFacadeAutorizacion.existAutorizacionByIdFactura(factura.getId())) {
//                currentAuth = ejbFacadeAutorizacion.findLastAutorizacionByIdFactura(factura.getId());
//                System.out.println("currentA: " + currentAuth);
//            }
//            autController.setCurrentAutorizacion(currentAuth);
//        } catch (Exception e) {
//            FacesContext context = FacesContext.getCurrentInstance();
//            context.addMessage(null, new FacesMessage("Error" + e));
//        }
        return "pretty:verfactura";
    }

    public String verAutorizacionDeFactura(Factura factura) {
        currentFactura = factura;
        flash.put("idFacturaFlash", currentFactura.getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objFactura", factura);

        return "pretty:listadoautorizacion";
    }

    public void imprimirAutorizacion() {
        if (currentAuth != null) {
            Reporte repo = new Reporte();
            ConverterUtilities objConvUtil = new ConverterUtilities();
            Voucher voucherEntity = ejbFacadeVoucher.findVoucherByIdAutorizacion(currentAuth.getId());
            VoucherClass voucherClass = objConvUtil.castToVoucher(voucherEntity.getTramaRespuesta());
            try {
                repo.reporteAutorizacion(currentAuth, voucherClass);
            } catch (JRException ex) {
                Logger.getLogger(FacturaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FacturaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void verAutorizaciones(Factura factura) {
        currentFactura = factura;
        try {
            autorizacionesByIdFactura = ejbFacadeAutorizacion.findAllAutorizacionesByIdFactura(factura.getId());
            existeAutorizacion = true;
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error" + e));
        }
    }

    public void verAutorizacionDeFactura(Autorizacion autorizacion) {
        autorizacionSelected = autorizacion;
        System.out.println("______________________________METODO");
//        try {
//            autController.setCurrentAutorizacion(autorizacion);
//
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idAutorizacionContext", autorizacion.getId());
//
//        } catch (Exception e) {
//            FacesContext context = FacesContext.getCurrentInstance();
//            context.addMessage(null, new FacesMessage("Error" + e));
//        }
//        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:verautorizacion");
    }

    public Autorizacion getAutorizacionSelected() {
        return autorizacionSelected;
    }

    public void setAutorizacionSelected(Autorizacion autorizacionSelected) {
        this.autorizacionSelected = autorizacionSelected;
    }

    public List<Autorizacion> getAutorizacionesByIdFactura() {
        return autorizacionesByIdFactura;
    }

    public void setAutorizacionesByIdFactura(List<Autorizacion> autorizacionesByIdFactura) {
        this.autorizacionesByIdFactura = autorizacionesByIdFactura;
    }

    public boolean isExisteAutorizacion() {
        return existeAutorizacion;
    }

    public void setExisteAutorizacion(boolean existeAutorizacion) {
        this.existeAutorizacion = existeAutorizacion;
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println("______________________________EVENT: " + event);
        autorizacionSelected = ((Autorizacion) event.getObject());
        System.out.println("_____________________________: " + autorizacionSelected);
    }

    public void metodoo(Autorizacion aut) {
        System.out.println(":::::::::::::::::::::::::::: METODO - 1" + aut);
        System.out.println(":::::::::::::::::::::::::::: METODO - 2" + aut.getCodAutorizacion());
    }

    public void onSelect(Autorizacion auth) {
        System.out.println("_____________________AUT: " + auth);
//        if (autorizacionSelected == null) {
//            autorizacionSelected = auth;
//        }
//        autorizacionSelected = auth;
    }

//    public void onSelect(Long idAuth) {
//        System.out.println("_____________________AUT: " + idAuth);
////        autorizacionSelected = auth;
//    }
    public void verAuth(Long id) {
        System.out.println("___________________________________ : " + id);
        flash.put("idAutorizacionFlash", id);
        System.out.println("______VER AUTH");
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:verautorizacion");
    }

    public FacturaDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(FacturaDataModel dataModel) {
        this.dataModel = dataModel;
    }
}
