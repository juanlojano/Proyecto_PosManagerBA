package com.baustro.mensajeria.jms;

import com.baustro.model.EstadoCierre;
import com.baustro.model.TerminalPinPad;
import com.baustro.model.CierreLote;
import com.baustro.sessionbean.CierreLoteFacade;
import com.baustro.util.MsgFile;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.jms.ExceptionListener;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import org.omg.CORBA.ExceptionList;

@Dependent
public class MensajeProducer {

    @Inject
    private JMSContext context;

    @Resource(mappedName = MensajeDefinicion.MENSAJE_QUEUE)
    private Queue syncQueue;

    @Inject
    private MensajeListener listener;

    @EJB
    private CierreLoteFacade cierreLoteFacade;

    public final static String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";

    // Defines the JMS context factory.
    public final static String JMS_FACTORY = "jms/TestConnectionFactory";

    // Defines the queue.
    public final static String QUEUE = "jms/TestJMSQueue";

    private QueueConnectionFactory qconFactory;
    private QueueConnection qcon;
    private QueueSession qsession;
    private QueueSender qsender;
    private Queue queue;
    private TextMessage msg;

    public String sendMensaje(List<?> txtMensaje) {
//        TextMessage txtMessage = 

//        context.createProducer().setAsync(listener).send(syncQueue, txtMensaje);
        return "Datos del mensaje: " + listener.getMessageID() + " , Fecha del envio " + (new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss")).format(Calendar.getInstance().getTime());
    }

    public void consumerPinpads() {
        ObjectMessage objMe = null;
        context.createProducer().setAsync(listener).send(syncQueue, "Texto");
    }

    public void consumerString(List<TerminalPinPad> lista) throws NamingException {
        for (TerminalPinPad terminalPinPad : lista) {
            int numMaxGrupo = cierreLoteFacade.findMaxGroupPinpad();
            CierreLote tpc = cierreLoteFacade.findByTidPinpadAndGrupo(terminalPinPad.getTid(), numMaxGrupo);
            tpc.setEstado(EstadoCierre.CERRANDO);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            tpc.setFechaCierre(dateFormat.format(date));
            tpc.setDescripcion("Estado Cierre: CERRANDO");
            cierreLoteFacade.edit(tpc);
            context.createProducer().setAsync(listener).send(syncQueue, terminalPinPad);
        }
    }

    public void consumerString(TerminalPinPad pp) throws NamingException {
        int numMaxGrupo = cierreLoteFacade.findMaxGroupPinpad();
        CierreLote tpc = cierreLoteFacade.findByTidPinpadAndGrupo(pp.getTid(), numMaxGrupo);
        tpc.setEstado(EstadoCierre.CERRANDO);
        tpc.setDescripcion("Estado Cierre: CERRANDO");
        cierreLoteFacade.edit(tpc);
        context.createProducer().setAsync(listener).send(syncQueue, pp);
    }

    private static InitialContext getInitialContext(String url)
            throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, url);
        return new InitialContext(env);
    }

}
