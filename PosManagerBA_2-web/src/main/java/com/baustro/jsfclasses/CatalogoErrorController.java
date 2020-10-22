package com.baustro.jsfclasses;

import com.baustro.jsfclasses.util.JsfUtil;
import com.baustro.jsfclasses.util.JsfUtil.PersistAction;
import com.baustro.sessionbean.CatalogoErrorFacade;
import com.baustro.model.CatalogoError;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("catalogoErrorController")
@RequestScoped
public class CatalogoErrorController  implements Serializable {

    @Inject
    private CatalogoErrorFacade ejbFacade;
    
    private List<CatalogoError> items = null;
    private CatalogoError selected;

    public CatalogoErrorController() {
    }

    public CatalogoError getSelected() {
        return selected;
    }

    public void setSelected(CatalogoError selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CatalogoErrorFacade getFacade() {
        return ejbFacade;
    }

    public CatalogoError prepareCreate() {
        selected = new CatalogoError();
        initializeEmbeddableKey();
        return selected;
    }

    public void create(CatalogoError ce) {
        System.out.println("_________________________: " + ejbFacade);
        selected = ce;
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CatalogoErrorCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        
        if (selected != null) {
            ejbFacade= new CatalogoErrorFacade();
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    System.out.println("_________________________: " + ejbFacade);
                    ejbFacade.edit(selected);
                } else {
                    ejbFacade.remove(selected);
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

    public CatalogoError getCatalogoError(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CatalogoError> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CatalogoError> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CatalogoError.class)
    public static class CatalogoErrorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CatalogoErrorController controller = (CatalogoErrorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "catalogoErrorController");
            return controller.getCatalogoError(getKey(value));
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
            if (object instanceof CatalogoError) {
                CatalogoError o = (CatalogoError) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CatalogoError.class.getName()});
                return null;
            }
        }

    }

}
