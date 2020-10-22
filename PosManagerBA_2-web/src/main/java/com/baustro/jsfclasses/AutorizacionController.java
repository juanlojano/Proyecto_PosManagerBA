package com.baustro.jsfclasses;

import com.baustro.classes.CustomException;
import com.baustro.classes.VoucherClass;
import com.baustro.interceptorbinding.LoggingInterceptorBinding;
import com.baustro.jsfclasses.util.EncriptaDesencriptarTrama;
import com.baustro.model.Autorizacion;
import com.baustro.model.EstadoAutorizacion;
import com.baustro.model.Factura;
import com.baustro.model.Parametros;
import com.baustro.model.TipoParametro;
import com.baustro.model.Voucher;
import com.baustro.service.AutorizacionFacadeREST;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.FacturaFacade;
import com.baustro.sessionbean.ParametrosFacade;
import com.baustro.sessionbean.VoucherFacade;
import com.baustro.utility.ConverterUtilities;
import com.baustro.utility.Reporte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Named("autorizacionController")
@ViewScoped
//@RequestScoped
@LoggingInterceptorBinding
//@Stateless
public class AutorizacionController implements Serializable {

    private List<Autorizacion> items = null;

    @EJB
    private AutorizacionFacade ejbFacade;

    @EJB
    private VoucherFacade ejbFacadeVoucher;

    @EJB
    private FacturaFacade ejbFacadeFactura;

    @EJB
    private AutorizacionFacade ejbFacadeAutorizacion;

    @EJB
    private ParametrosFacade parametrosFacade;

    @EJB
    private AutorizacionFacadeREST ejbFacadeAutorizacionRest;

    private Autorizacion selected;
    private Autorizacion selectedAuth;

    private Autorizacion currentAutorizacion;
    private List<Autorizacion> autorizacionesByIdFactura;

    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private Long idAutorizacionFlash;
    private Long idFacturaFlash;
    private Factura facturaCurrentAuth;
    private String codigoFacturaTemporal;
    private Factura objFactura;
    private AutorizacionDataModel dataModel;

    Factura fact = new Factura();

    public AutorizacionController() {
    }

    @PostConstruct
    public void init() {
        dataModel = new AutorizacionDataModel(ejbFacade);
        idAutorizacionFlash = (Long) flash.get("idAutorizacionFlash");
        if (idFacturaFlash == null) {
            idFacturaFlash = (Long) flash.get("idFacturaFlash");
        }

        if (objFactura == null) {
            objFactura = (Factura) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("objFactura");
        }

        Long idAutContext = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idAutorizacionContext");
//        fact = (Factura) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("objFactura");

        if (idAutContext != null) {
            currentAutorizacion = ejbFacade.FindAutorizacionById(idAutContext);

        }
        if (idAutorizacionFlash != null) {
            currentAutorizacion = ejbFacade.FindAutorizacionById(idAutorizacionFlash);
        }

        if (objFactura != null) {
            facturaCurrentAuth = objFactura;
            codigoFacturaTemporal = facturaCurrentAuth.getCodFactura();
        }

        if (idFacturaFlash != null) {
            facturaCurrentAuth = ejbFacadeFactura.FindFacturaById(idFacturaFlash);
            codigoFacturaTemporal = facturaCurrentAuth.getCodFactura();
//            currentAutorizacion = ejbFacade.FindAutorizacionById(idAutorizacionFlash);
        }
    }

    public String obtenerCodigoFactura() {
        if (codigoFacturaTemporal != null) {
            return codigoFacturaTemporal;
        } else {
            return null;
        }
    }

    public Autorizacion getSelected() {
        return selected;
    }

    public void setSelected(Autorizacion selected) {
        this.selected = selected;
    }

    public Autorizacion getCurrentAutorizacion() {

        return currentAutorizacion;
    }

    public void setCurrentAutorizacion(Autorizacion currentAutorizacion) {
        this.currentAutorizacion = currentAutorizacion;
    }

    public LazyDataModel<Autorizacion> getModel() {
        String msg = (String) flash.get("siAnulada");
        if (msg != null && msg.compareTo("siAnulada") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                    + "                            title: 'Cambios realizados',\n"
                    + "                            message: 'Autorización anulada satisfactoriamente!',\n"
                    + "                        });");

            msg = null;
        }

        String msgNoAnull = (String) flash.get("noAnulada");
        if (msgNoAnull != null && msgNoAnull.compareTo("noAnulada") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.error({\n"
                    + "                            title: 'Error',\n"
                    + "                            message: 'No se ha podido realizar la anulación!',\n"
                    + "                        });");

            msgNoAnull = null;
        }

        if (items == null && objFactura == null) {

            return dataModel;
        }

        return null;

//        if (objFactura != null) {
//            autorizacionesByIdFactura = ejbFacadeAutorizacion.findAllAutorizacionesByIdFactura(objFactura.getId());
////            dataModel = (AuthLazyDataModel) autorizacionesByIdFactura;
//
//            return dataModel;
//        } else {
//
//            return dataModel;
//        }
    }

    public List<Autorizacion> obtenerItemsAutorizacion() {
        String indicador = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indicador");

        String msg = (String) flash.get("siAnulada");
        if (msg != null && msg.compareTo("siAnulada") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                    + "                            title: 'Cambios realizados',\n"
                    + "                            message: 'Autorización anulada satisfactoriamente!',\n"
                    + "                        });");

            msg = null;
        }

        String msgNoAnull = (String) flash.get("noAnulada");
        if (msgNoAnull != null && msgNoAnull.compareTo("noAnulada") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.error({\n"
                    + "                            title: 'Error',\n"
                    + "                            message: 'No se ha podido realizar la anulación!',\n"
                    + "                        });");

            msgNoAnull = null;
            
        }

        if (objFactura != null) {
            autorizacionesByIdFactura = ejbFacadeAutorizacion.findAllAutorizacionesByIdFactura(objFactura.getId());
            return autorizacionesByIdFactura;
        } else {
            return items;
        }
    }

    public Autorizacion getAutorizacion(java.lang.Long id) {

        return ejbFacade.find(id);
    }

    public void metodoPrueba(Autorizacion aut) {
        System.out.println("------------------_______________________________EN METODO PRUEBA");
        System.out.println("------------------_______________________________EN METODO PRUEBA: " + aut.getCodAutorizacion());
    }

    public void verAutorizacion(Autorizacion autorizacion) {
        try {
            currentAutorizacion = autorizacion;
            flash.put("idAutorizacionFlash", currentAutorizacion.getId());

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoAutorizacion", currentAutorizacion);
//        return "pretty:verautorizacionautorizacion";
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:verautorizacionautorizacion");
        } catch (Exception e) {

        }
    }

    public boolean validarImpresionAutorizacion() {
        currentAutorizacion = (Autorizacion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("objetoAutorizacion");
        if (currentAutorizacion != null) {
            if (currentAutorizacion.getFactura() == null
                    || currentAutorizacion.getFactura().getCodFactura() == null) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public String validarMensajeImpresion() {
        currentAutorizacion = (Autorizacion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("objetoAutorizacion");
        if (currentAutorizacion.getCodAutorizacion() == null
                || currentAutorizacion.getFactura() == null
                || currentAutorizacion.getFactura().getCodFactura() == null) {
            return "No se puede imprimir esta autorización - Existen campos necesarios que estan vacíos";
        } else {
            return "Imprimir autorización actual";
        }
    }

    public void imprimirAutorizacion() {
        currentAutorizacion = (Autorizacion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("objetoAutorizacion");
        try {
            Reporte repo = new Reporte();
            ConverterUtilities objConvUtil = new ConverterUtilities();
            Voucher voucherEntity = ejbFacadeVoucher.findVoucherByIdAutorizacion(currentAutorizacion.getId());
            VoucherClass voucherClass = objConvUtil.castToVoucher(voucherEntity.getTramaRespuesta());
            repo.reporteAutorizacion(currentAutorizacion, voucherClass);

            RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                    + "                            title: 'Impresión Existosa',\n"
                    + "                            message: 'Registro impreso satisfactoriamente',\n"
                    + "                        });");

        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        }
    }

    private void redireccionarPaginaError(Exception e) {
        CustomException customException = new CustomException(e);
        customException = customException.retornarCustomException();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoCustomException", customException);
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:errorPage");
//            CatalogoError catalogoError = ejbErrorFacade.FindErrorByCodigo(customException.getCodigo());
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoCatalogoError", catalogoError);
    }

    public void navToFacturas() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarfactura");
    }

    public List<Autorizacion> getAutorizacionesByIdFactura() {
        return autorizacionesByIdFactura;
    }

    public void setAutorizacionesByIdFactura(List<Autorizacion> autorizacionesByIdFactura) {
        this.autorizacionesByIdFactura = autorizacionesByIdFactura;
    }

    public Factura getFacturaCurrentAuth() {
        return facturaCurrentAuth;
    }

    public void setFacturaCurrentAuth(Factura facturaCurrentAuth) {
        this.facturaCurrentAuth = facturaCurrentAuth;
    }

    public void redirect() throws IOException {
        // ...

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("pretty:listarfactura");
    }

    public String obtenerDescripcionPlazoDiferido() {
        Parametros parametro = new Parametros();
        try {
            parametro = ejbFacadeAutorizacion.obtenerDescripcionParametro(currentAutorizacion.getPlazoDiferido(), TipoParametro.PLAZO_DIFERIDO);
        } catch (Exception e) {
            redireccionarPaginaError(e);
        }

        if (parametro != null) {
            return parametro.getParamDescripcion();
        } else {
            return "";
        }

    }

    public String obtenerDescripcionTipoDiferido() {
        Parametros parametro = new Parametros();
        try {
            parametro = ejbFacadeAutorizacion.obtenerDescripcionParametro(currentAutorizacion.getTipoDiferido(), TipoParametro.TIPO_DIFERIDO);
        } catch (Exception e) {
            System.err.println("ERROR:_  " + e);
            e.printStackTrace();
            redireccionarPaginaError(e);
        }

        if (parametro != null) {
            return parametro.getParamDescripcion();
        } else {
            return "";
        }
    }

    private StreamedContent streamedContent = new DefaultStreamedContent(getData(), "application/pdf", "_PDF");

    public StreamedContent getStreamedContent() {
//        StreamedContent streamedContentTmp = (StreamedContent) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("streamedContentFacesContext");
//        streamedContent = streamedContentTmp;

        return streamedContent;
    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }

    public String generateRandomIdForNotCaching() {
        return java.util.UUID.randomUUID().toString();
    }

    public void imprimirEnPDF() { 
        System.out.println("---------- : " + parametrosFacade);
        currentAutorizacion = (Autorizacion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("objetoAutorizacion");
        try {
            streamedContent = createStream();
            setStreamedContent(streamedContent);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("streamedContentFacesContext", streamedContent);

            Reporte repo = new Reporte();
            ConverterUtilities objConvUtil = new ConverterUtilities();
            Voucher voucherEntity = ejbFacadeVoucher.findVoucherByIdAutorizacion(currentAutorizacion.getId());
            VoucherClass voucherClass = objConvUtil.castToVoucher(EncriptaDesencriptarTrama.Desencriptar(voucherEntity.getTramaRespuesta()));
            repo.reporteAutorizacion(currentAutorizacion, voucherClass);

            RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                    + "                            title: 'Impresión Existosa',\n"
                    + "                            message: 'Registro impreso satisfactoriamente',\n"
                    + "                        });");

        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        }
    }

    private StreamedContent createStream() {
        streamedContent = new DefaultStreamedContent(getData(), "application/pdf", "_PDF");
        return streamedContent;
    }

    private InputStream getData() {

        // pdf files under src\main\resources
        File file = new File(System.getProperty("user.dir") + "\\reporteAutorizacion.pdf");

        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            redireccionarPaginaError(e);
        }

        return is;

    }

    public Boolean verificarEstadoAutorizacion(Autorizacion autorizacion) {
        try {
            if (autorizacion.getEstadoAutorizacion().compareTo(EstadoAutorizacion.ANULADA) == 0
                    || autorizacion.getEstadoAutorizacion().compareTo(EstadoAutorizacion.INCONSISTENTE) == 0
                    || autorizacion.getEstadoAutorizacion().compareTo(EstadoAutorizacion.REVERSO) == 0
                    || autorizacion.getEstadoAutorizacion().compareTo(EstadoAutorizacion.REVERSO_AUTOMATICO) == 0
                    || autorizacion.getCodAutorizacion() == null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            redireccionarPaginaError(e);
        }

        return true;
    }

    public String mostrarTituloAutorizacion(Autorizacion autorizacion) {
        try {
            if (autorizacion.getEstadoAutorizacion().compareTo(EstadoAutorizacion.ANULADA) == 0
                    || autorizacion.getCodAutorizacion() == null) {
                return "No se puede anular esta autorización";
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idAutorizacionFacesContext", autorizacion.getId());

                return "Anular autorizacion";
            }
        } catch (Exception e) {
            redireccionarPaginaError(e);
        }

        return "";
    }

    public void anularAutorización() {
        try {
            Long a = selectedAuth.getId();
//            Long idAuth = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idAutorizacionFacesContext");
            Long idAuth = (Long) selectedAuth.getId();;

            if (idAuth != null) {
                currentAutorizacion = ejbFacade.FindAutorizacionById(idAuth);
                if (ejbFacadeAutorizacionRest.posmanagerAnulacion(currentAutorizacion) == true) {
                    flash.put("siAnulada", "siAnulada");
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listadoautorizacion");
                } else {
                    flash.put("noAnulada", "noAnulada");
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listadoautorizacion");
                }
            }
        } catch (Exception e) {
            redireccionarPaginaError(e);
        }
    }

    public void onSelect(Long idAutorizacion) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idAutorizacionFacesContext", idAutorizacion);
    }

    public void onRowSelect(SelectEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idAutorizacionFacesContext", ((Autorizacion) event.getObject()).getId());
    }

    public Autorizacion getSelectedAuth() {
        return selectedAuth;
    }

    public void setSelectedAuth(Autorizacion selectedAuth) {
        this.selectedAuth = selectedAuth;
    }

    public List<Autorizacion> obtenerAutorizacionesByIdLote(Long id) {

        return ejbFacade.findAllAutorizacionesByIdLote(id);
    }

    public List<Autorizacion> obtenerAutorizacionesOkByIdLote(Long idLote) {

        return ejbFacade.findAllAutorizacionesOkByIdLote(idLote);
    }

    public List<Autorizacion> obtenerAutorizacionesInconsistentesByIdLote(Long idLote) {

        return ejbFacade.findAllAutorizacionesInconsistencesByIdLote(idLote);
    }

}
