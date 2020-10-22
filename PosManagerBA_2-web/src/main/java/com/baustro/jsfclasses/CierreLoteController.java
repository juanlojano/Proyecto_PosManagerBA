package com.baustro.jsfclasses;

import com.baustro.model.TerminalPinPad;
import com.baustro.mensajeria.jms.MensajeProducer;
import com.baustro.model.EstadoCierre;
import com.baustro.model.CierreLote;
import com.baustro.sessionbean.LoteFacade;
import com.baustro.sessionbean.TerminalCajaFacade;
import com.baustro.sessionbean.TerminalPinPadFacade;
import com.baustro.sessionbean.CierreLoteFacade;
import com.baustro.utility.ConverterUtilities;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.naming.NamingException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@Named("cierreLoteController")
@RequestScoped
//@Stateless
public class CierreLoteController extends AbstractController {

    private TerminalPinPad current;
    private List<TerminalPinPad> items = null;
    private int selectedItemIndex;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    private List<TerminalPinPad> pinpadsParaCierre = new ArrayList<TerminalPinPad>();
    private List<String> pps = new ArrayList<String>();
    private TerminalPinPadDataModel dataModel;
    private List<String> msjs = new ArrayList<>();

    @EJB
    private TerminalPinPadFacade ejbFacade;

    @EJB
    private CierreLoteFacade cierreLoteFacade;

    @EJB
    private TerminalCajaFacade ejbTerminalCajaFacade;

    @Inject
    ComercioController comercioInject;

    @Inject
    private ConverterUtilities converter;

    @Inject
    LoteController loteController;

    @Inject
    private LoteFacade loteFacade;

    @Inject
    private MensajeProducer mensajeProducer;

    private TerminalPinPadFacade getFacade() {
        return ejbFacade;
    }
    private int number;

    public int getNumber() {
        return number;
    }

    public void increment() {
        number++;
    }

    @PostConstruct
    public void init() {
        dataModel = new TerminalPinPadDataModel(ejbFacade);
    }

    public CierreLoteController() {
    }

    public List<TerminalPinPad> getPinpadsParaCierre() {
        return pinpadsParaCierre;
    }

    public void setPinpadsParaCierre(List<TerminalPinPad> pinpadsParaCierre) {
        this.pinpadsParaCierre = pinpadsParaCierre;
    }

    public List<TerminalPinPad> getItems() {
        if (items == null) {
            items = ejbFacade.findAllNoNull();
            if (items != null) {
                setearVariablesFlash();
            }
        }

        return items;
    }

    public TerminalPinPad getTerminalPinPad(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public LazyDataModel<TerminalPinPad> getModel() {

        return dataModel;
    }

    public TerminalPinPad getSelected() {
        if (current == null) {
            current = new TerminalPinPad();
            selectedItemIndex = -1;
        }
        return current;
    }

    public void cerrarPinpads() throws JMSException, FileNotFoundException, IOException {
        msjs = new ArrayList<>();
        try {
            if (pinpadsParaCierre.isEmpty()) {
                RequestContext.getCurrentInstance().execute("iziToast.error({\n"
                        + "                            title: 'ERROR',\n"
                        + "                            message: 'No se ha seleccionado ningun dispositivo!',\n"
                        + "                        });");
            } else if (validarCierre(pinpadsParaCierre)) {
                RequestContext.getCurrentInstance().execute("PF('dlg').show();");
                iniciarCierre(pinpadsParaCierre);
                cerrar(pinpadsParaCierre);
                int numGrupo = cierreLoteFacade.findMaxGroupPinpad();
//                Boolean grupoValido = cierreLoteFacade.findCierreByIdGrupoAndEstado(numGrupo);
//                if (grupoValido == true) {
//                    RequestContext.getCurrentInstance().execute("PF('dlg').hide();");
//                }

            } else {
                RequestContext.getCurrentInstance().execute("iziToast.error({\n"
                        + "                            title: 'ERROR',\n"
                        + "                            message: 'Ha sucedido un error en el proceso de cierre!',\n"
                        + "                        });");
            }
        } catch (Exception ex) {
            Logger.getLogger(TerminalPinPadController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validarCierre(List<TerminalPinPad> pinpadsCierre) {
        boolean validaGrupo = false;
        int numeroCierres = cierreLoteFacade.count();

        if (numeroCierres == 0) {
            validaGrupo = true;
        } else {
            int numGrupo = cierreLoteFacade.findMaxGroupPinpad();
            Boolean grupoValido = cierreLoteFacade.findCierreByIdGrupoAndEstado(numGrupo);
            if (grupoValido) {
                validaGrupo = true;
            } else {
                validaGrupo = false;
            }
        }

        return validaGrupo;
    }

    private void iniciarCierre(List<TerminalPinPad> pinpadsCierre) {
        int cantidadCierres = cierreLoteFacade.count();
        int numGrupo = 0;
        if (cantidadCierres == 0) {
            numGrupo = 1;
        } else {
            int numMaxGrupo = cierreLoteFacade.findMaxGroupPinpad();
            numGrupo = numMaxGrupo + 1;
        }
        for (TerminalPinPad pp : pinpadsCierre) {
            CierreLote tpc = new CierreLote();
            tpc.setIp(pp.getIp());
            tpc.setTid(pp.getTid());
            tpc.setEstado(EstadoCierre.INICIADO);
            tpc.setGrupo(numGrupo);
            tpc.setDescripcion("Estado Cierre: INICIADO");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            tpc.setFechaCierre(dateFormat.format(date));
            cierreLoteFacade.create(tpc);
        }

    }

    private void cerrar(List<TerminalPinPad> pinpadsParaCierre) {
        Thread thread = new Thread();
        try {
            mensajeProducer.consumerString(pinpadsParaCierre);
        } catch (NamingException ex) {
            Logger.getLogger(CierreLoteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int numGrupo = cierreLoteFacade.findMaxGroupPinpad();
        int contador = 0;
        try {
            thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CierreLoteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Boolean cerradoGrupo = cierreLoteFacade.findCierreByIdGrupoAndEstado(numGrupo);
        while (cerradoGrupo == false) {
            contador++;
            List<CierreLote> listaUltimoGrupoCierreNoCompletado = cierreLoteFacade.findAllLastGroupNotComplete(numGrupo);
            List<TerminalPinPad> listaPPUltimoGrupoCierreNoCompletado = new ArrayList<>();
            for (CierreLote cierreLote : listaUltimoGrupoCierreNoCompletado) {
                TerminalPinPad tp = ejbFacade.findTerminalPinPadByTID(cierreLote.getTid());
                listaPPUltimoGrupoCierreNoCompletado.add(tp);
            }
            try {
                mensajeProducer.consumerString(listaPPUltimoGrupoCierreNoCompletado);
            } catch (NamingException ex) {
                Logger.getLogger(CierreLoteController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (contador == 5) {
                for (CierreLote cierreLote : listaUltimoGrupoCierreNoCompletado) {
                    cierreLote.setEstado(EstadoCierre.NO_CERRADO);
                    cierreLote.setDescripcion("Estado cierre: COMPLETO (forzado)");
                    cierreLoteFacade.edit(cierreLote);
                }
            }
            cerradoGrupo = cierreLoteFacade.findCierreByIdGrupoAndEstado(numGrupo);
        }
        thread.interrupt();
    }

    public TerminalPinPadDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(TerminalPinPadDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void generarArchivoCierre() {
        ejbTerminalCajaFacade.generarArchivoCierre();
        RequestContext.getCurrentInstance().execute("iziToast.success({\n"
                + "                            title: 'Realizado',\n"
                + "                            message: 'Archivo de cierre generadosatisfactoriamente!',\n"
                + "                        });");

    }

    public List<String> getPps() {
        return pps;
    }

    public void setPps(List<String> pps) {
        this.pps = pps;
    }

    public CierreLoteFacade getTerminalPinPadFacade_Cierre() {
        return cierreLoteFacade;
    }

    public void setTerminalPinPadFacade_Cierre(CierreLoteFacade terminalPinPadFacade_Cierre) {
        this.cierreLoteFacade = terminalPinPadFacade_Cierre;
    }

    public void cerrarDialog() {

        pinpadsParaCierre = new ArrayList<>();
    }

    public void mostrarMsjCierre() {
        msjs = new ArrayList<>();
        try {
            if (!pinpadsParaCierre.isEmpty()) {
                try {
                    int numGrupo = cierreLoteFacade.findMaxGroupPinpad();
                    List<CierreLote> listadoCierre = new ArrayList<>();
                    listadoCierre = cierreLoteFacade.findAllLastGroup(numGrupo);
                    if (listadoCierre != null) {
                        if (listadoCierre.isEmpty()) {
                            msjs = new ArrayList<>();
                        } else {
                            for (CierreLote cierreLote : listadoCierre) {
                                setearMsj(cierreLote.getIp() + " : " + cierreLote.getDescripcion());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TerminalPinPadController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CierreLote> obtenerLogCierres() {
        List<CierreLote> lista = cierreLoteFacade.findAll();

        return lista;
    }

    public List<String> getMsjs() {
        return msjs;
    }

    public void setMsjs(List<String> msjs) {
        this.msjs = msjs;
    }

    private void setearMsj(String mensaje) {
        msjs.add(mensaje);
    }

    public String getTime() {
        return LocalTime.now().toString();
    }
}
