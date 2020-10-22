package com.baustro.jsfclasses;

import com.baustro.model.Lote;
import com.baustro.jsfclasses.util.JsfUtil;
import com.baustro.jsfclasses.util.PaginationHelper;
import com.baustro.model.Autorizacion;
import com.baustro.model.EstadoAutorizacion;
import com.baustro.sessionbean.AutorizacionFacade;
import com.baustro.sessionbean.LoteFacade;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@Named("loteController")
@RequestScoped
public class LoteController extends AbstractController {

    private Lote current;
    private List<Lote> items = null;

    @EJB
    private AutorizacionFacade autorizacionFacade;

    @EJB
    private LoteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private LoteDataModel dataModel;

    public LoteController() {
    }

    @PostConstruct
    public void init() {
        System.out.println("--------------POSTCONST0--LOTE: " + ejbFacade);
        dataModel = new LoteDataModel(ejbFacade);
    }

    public Lote getSelected() {
        if (current == null) {
            current = new Lote();
            selectedItemIndex = -1;
        }
        return current;
    }

    private LoteFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareCreate() {
        current = new Lote();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LoteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LoteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public List<Lote> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
        }
        return items;
    }

    public Lote getLote(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    private void persist(JsfUtil.PersistAction persistAction) {
        setEmbeddableKeys();
        try {
            if (persistAction == JsfUtil.PersistAction.CREATE) {
                ejbFacade.create(current);
            }
            if (persistAction == JsfUtil.PersistAction.UPDATE) {
                ejbFacade.edit(current);
            }
            if (persistAction == JsfUtil.PersistAction.DELETE) {
                ejbFacade.edit(current);
            }
        } catch (Exception ex) {
            redireccionarPaginaError(ex);
        }
    }

    public String obtenerValorTotalLote(Lote lote) {
        Double totalLote = 0.0;
        List<Autorizacion> listaAutorizaciones = new ArrayList<Autorizacion>();
        listaAutorizaciones = autorizacionFacade.findAllAutorizacionesByIdLote(lote.getId());

        for (Autorizacion autorizacion : listaAutorizaciones) {
            if (autorizacion.getEstadoAutorizacion() == EstadoAutorizacion.NORMAL) {
                totalLote = totalLote + Double.parseDouble(String.valueOf(autorizacion.getTotalVenta()));
            }
        }
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(totalLote);
    }

    public LazyDataModel<Lote> getModel() {

        return dataModel;
    }

    public LoteDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(LoteDataModel dataModel) {
        this.dataModel = dataModel;
    }
}
