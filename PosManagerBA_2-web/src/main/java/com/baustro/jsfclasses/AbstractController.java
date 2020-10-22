/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.jsfclasses;

import com.baustro.classes.CustomException;
import com.baustro.model.CatalogoError;
import com.baustro.sessionbean.ErrorFacade;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ba0100063v
 */

public abstract class AbstractController {

    @EJB
    private ErrorFacade ejbErrorFacade;

    @Inject
    private CatalogoError catalogoError;

    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

    public void redireccionarPaginaError(Exception e) {
        CustomException customException = new CustomException(e);
        customException = customException.retornarCustomException();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoCustomException", customException);
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:errorPage");
    }

    public CatalogoError devolverDetalleError(Exception e) {
        CustomException customException = new CustomException(e);
        customException = customException.retornarCustomException();
        catalogoError = ejbErrorFacade.FindErrorByCodigo(customException.getCodigo());

        return catalogoError;
    }

    public void setearVariablesFlash() {
        String persist = null;
        String updated = null;
        String deleted = null;
        String noDeleted = null;
        String siAnulada = null;
        try {
            persist = (String) flash.get("persist");
            getMensajes(persist);
            updated = (String) flash.get("updated");
            getMensajes(updated);
            deleted = (String) flash.get("deleted");
            getMensajes(deleted);
            noDeleted = (String) flash.get("noDeleted");
            getMensajes(noDeleted);
            siAnulada = (String) flash.get("siAnulada");
            getMensajes(siAnulada);
        } catch (Exception e) {
            redireccionarPaginaError(e);
        }
    }

    public void getMensajes(String msg) {
        if (msg != null && msg.compareTo("persist") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                    + "                            title: 'Cambios realizados',\n"
                    + "                            message: 'Registro creado satisfactoriamente!',\n"
                    + "                        });");

            msg = null;
        }

        if (msg != null && msg.compareTo("updated") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                    + "                            title: 'Cambios realizados',\n"
                    + "                            message: 'Registro actualizado satisfactoriamente!',\n"
                    + "                        });");

            msg = null;
        }

        if (msg != null && msg.compareTo("deleted") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                    + "                            title: 'Eliminado',\n"
                    + "                            message: 'Registro eliminado satisfactoriamente!',\n"
                    + "                        });");

            msg = null;
        }

        if (msg != null && msg.compareTo("noDeleted") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.error({\n"
                    + "                            title: 'Error',\n"
                    + "                            message: 'No se puede eliminar!',\n"
                    + "                        });");

            msg = null;
        }
        if (msg != null && msg.compareTo("siAnulada") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                    + "                            title: 'Cambios realizados',\n"
                    + "                            message: 'Autorización anulada satisfactoriamente!',\n"
                    + "                        });");

            msg = null;
        }
        if (msg != null && msg.compareTo("pinpadsParaCierreVacio") == 0) {
            RequestContext.getCurrentInstance().execute("iziToast.Warning({\n"
                    + "                            title: 'Advertencia',\n"
                    + "                            message: 'No existen pinpads seleccionados!',\n"
                    + "                        });");

            msg = null;
        }
    }

    public void setEmbeddableKeys() {
    }

}
