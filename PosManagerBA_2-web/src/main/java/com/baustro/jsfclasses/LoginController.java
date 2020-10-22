package com.baustro.jsfclasses;

import com.baustro.classes.CustomException;
import com.baustro.jsfclasses.util.EncriptaDesencriptarTrama;
import com.baustro.mensajeria.jms_cola_mensajeria.C_M_MensajeProducer;
import com.baustro.mensajeria.jms_cola_mensajeria.C_M_MensajeConsumer;
import com.baustro.model.CatalogoError;
import com.baustro.sessionbean.ErrorFacade;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Named("loginController")
@RequestScoped
public class LoginController extends AbstractController implements Serializable {

    @Inject
    private C_M_MensajeProducer c_m_mensajeProducer;

    @Inject
    private C_M_MensajeConsumer c_M_MensajeConsumer;

    private String campoLimpiar;
    private boolean collapsed;

    int CONTADORINTENTOS = 0;

    public String getCampoLimpiar() {
        return campoLimpiar;
    }

    public void setCampoLimpiar(String campoLimpiar) {
        this.campoLimpiar = campoLimpiar;
    }

    private String codigoErrorFlash;
    private CustomException objetoCustomException;
    private CatalogoError objetoCatalogoError;
    private String username;
    private String password;
    int contador = 0;
    private String contadorIntentosString = null;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    FacesContext ctx = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
    private int number;

    @EJB
    private ErrorFacade ejbErrorFacade;
    private Boolean collapseFacesContext;

    @Inject
    private CatalogoErrorController catalogoErrorController;

    @PostConstruct
    public void init() {
        contadorIntentosString = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contador");
        if (contadorIntentosString != null) {
            if (contadorIntentosString.compareTo("5") == 0) {
                contadorIntentosString = "1";
            }
        }
        collapseFacesContext = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("collapseFacesContext");
        if (collapseFacesContext != null) {
            collapsed = collapseFacesContext;
        }
        objetoCustomException = (CustomException) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("objetoCustomException");
        if (objetoCustomException != null && objetoCustomException.getCodigo() != null) {
            objetoCatalogoError = ejbErrorFacade.FindErrorByCodigo(objetoCustomException.getCodigo());
            if (objetoCatalogoError == null) {
                objetoCatalogoError = new CatalogoError();
            }
            objetoCustomException = new CustomException();
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse res) {
        request.getSession().invalidate();
    }

    public CatalogoError getObjetoCatalogoError() {
        return objetoCatalogoError;
    }

    public void setObjetoCatalogoError(CatalogoError objetoCatalogoError) {
        this.objetoCatalogoError = objetoCatalogoError;
    }

    public CustomException getObjetoCustomException() {
        return objetoCustomException;
    }

    public void setObjetoCustomException(CustomException objetoCustomException) {
        this.objetoCustomException = objetoCustomException;
    }

    public String getCodigoErrorFlash() {
        return codigoErrorFlash;
    }

    public void setCodigoErrorFlash(String codigoErrorFlash) {
        this.codigoErrorFlash = codigoErrorFlash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void cancel() {

        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:index");
    }

    public void jmsP() throws IOException {
        try {
            c_m_mensajeProducer.sendMensaje("MENSAJE desde login controller");
        } catch (JMSException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void jmsC() throws IOException, JMSException {
        c_M_MensajeConsumer.onMessage();
    }

    public String volverHome() {

        return "pretty:listadoautorizacion";
    }

    public void navToAuth() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objFactura", null);
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listadoautorizacion");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", true);
    }

    public void navToFacturas() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarfactura");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", true);
    }

    public void navToComercio() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarcomercio");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", true);
    }

    public void navToCaja() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarcaja");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", true);
    }

    public void navToPinpad() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarpinpad");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", true);
    }

    public void navToLote() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarLote");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", true);
    }

    public void navToCierrePinpad() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarpinpadcierre");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", true);
    }

    public void navToConciliacion() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarConciliacion");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", true);
    }

    public void navToInstitucion() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarinstitucion");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", false);
    }

    public void navToPlazo() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarplazo");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", false);
    }

    public void navToTDiferido() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listartipodiferido");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", false);
    }

    public void navToTPago() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listartipopago");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", false);
    }

    public void navToBin() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:listarbin");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", false);
    }

    public void navToLogin() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:login");
    }

    public void navToLoginError() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:loginError");
    }

    public void login() {
        int contadorIntentos = 0;
        try {
            if (contadorIntentosString != null) {
                contadorIntentos = Integer.parseInt(contadorIntentosString);
            } else {
                contadorIntentos = 1;
            }

            if (contadorIntentos == 4) {
                navToLoginError();
                contadorIntentos++;
            } else {
                try {
                    if (request.getUserPrincipal() == null) {
                        request.login(username, password);
                        if (request.isUserInRole("ADMIN")) {
                            navToComercio();
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("collapseFacesContext", true);
                        } else {
                            navToLogin();
                        }
                    } else {
                        navToAuth();
                    }
                } catch (ServletException ex) {
                    System.out.println(":::::::::::::::::::::: " + ex.getMessage());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Inicio de sesión fallido, error de usuario o clave", null));
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    if (ex.getMessage().contains("Login failed")) {
                        contadorIntentos++;
                    }
                }
            }
            String contadorIntentosString = String.valueOf(contadorIntentos);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contador", contadorIntentosString);

        } catch (Exception ex) {
            System.out.println("EX: " + ex.getMessage());
        }
    }

    public String getUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Principal user = request.getUserPrincipal();

        return user.toString();
    }

    public String metodoListener() {

//        C_M_MensajeConsumer obj= new C_M_MensajeConsumer();
//        
//        SimuladorPinpad_Servidor simulador = new SimuladorPinpad_Servidor();
//        simulador.interrupt();
//        if (!simulador.isAlive()) {
//            simulador.start();
//        }
//
//        estadoSocket = "state: " + simulador.getState().name()
//                + "   -   isAlive:" + simulador.isAlive()
//                + "   -   isInterrupted: " + simulador.isInterrupted();
//        return estadoSocket;
        return "";
    }

    public void insertarError() {
        CatalogoError catalogo = new CatalogoError();
        catalogo.setCodError("01");
        catalogo.setDescripcionTecnica("D T");
        catalogo.setDescripcionUsuario("D U");
        catalogoErrorController.create(catalogo);
    }

    public void provocarError() {
        try {
            System.out.println("Intentamos ejecutar el bloque de instrucciones:");
            System.out.println("Instrucción 1.");
            int n = Integer.parseInt("M"); //error forzado en tiempo de ejecución.
            System.out.println("Instrucción 2.");
            System.out.println("Instrucción 3, etc.");
        } catch (Exception e) {
            redireccionarPaginaError(e);
        }
    }

    public void limpiar() {

    }

    public void pruebaEncriptar() throws Exception {
//        String a = EncriptaTrama.Encriptar("00D4PP01300     000000001000000000004400000000000000000000000600                                          11442420180227      000000332976   00165787                                   4E2D28D68312D0163CAA5F1509ED1156");
        String a = EncriptaDesencriptarTrama.Encriptar("hola mundo");
        System.out.println("---------------------------------: " + a);
        String b = EncriptaDesencriptarTrama.Desencriptar(a);
        System.out.println("---------------------------------: " + b);
    }

    public Boolean getCollapsed() {

        return collapsed;
    }

    public int getNumber() {
        return number;
    }

    public void increment() {
        number++;
    }

    public String getContadorIntentosString() {
        return contadorIntentosString;
    }

    public void setContadorIntentosString(String contadorIntentosString) {
        this.contadorIntentosString = contadorIntentosString;
    }

}
