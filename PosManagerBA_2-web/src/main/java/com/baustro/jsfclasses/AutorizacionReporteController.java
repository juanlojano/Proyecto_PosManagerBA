package com.baustro.jsfclasses;

import com.baustro.classes.CustomException;
import com.baustro.interceptorbinding.LoggingInterceptorBinding;
import com.baustro.model.Autorizacion;
import com.baustro.model.Factura;
import com.baustro.service.AutorizacionFacadeREST;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.FacturaFacade;
import com.baustro.sessionbean.VoucherFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named("autorizacionReporteController")
@RequestScoped
@LoggingInterceptorBinding
//@Stateless
public class AutorizacionReporteController implements Serializable {

    private StreamedContent streamedContent = new DefaultStreamedContent(getData(), "application/pdf", "_PDF");

    public AutorizacionReporteController() {
    }

    @PostConstruct
    public void init() {
    }

    public StreamedContent getStreamedContent() {
        try {
            StreamedContent streamedContentTmp = (StreamedContent) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("streamedContentFacesContext");
            streamedContent = streamedContentTmp;

            return streamedContent;

        } catch (Exception e) {
            return null;
        }

    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }

    public String generateRandomIdForNotCaching() {
        return java.util.UUID.randomUUID().toString();
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

    private void redireccionarPaginaError(Exception e) {
        CustomException customException = new CustomException(e);
        customException = customException.retornarCustomException();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoCustomException", customException);
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:errorPage");
//            CatalogoError catalogoError = ejbErrorFacade.FindErrorByCodigo(customException.getCodigo());
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoCatalogoError", catalogoError);
    }

}
