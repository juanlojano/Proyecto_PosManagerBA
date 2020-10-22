package com.baustro.mensajeria.jms;

import com.baustro.bean.almacenes.ManejadorColaAlmacenes;
import com.baustro.model.EstadoCierre;
import com.baustro.model.TerminalPinPad;
import com.baustro.model.CierreLote;
import com.baustro.sessionbean.CierreLoteFacade;
import com.baustro.utility.ConverterUtilities;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

@MessageDriven(name = "MensajeReceiver", activationConfig = {
    @javax.ejb.ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = MensajeDefinicion.MENSAJE_QUEUE),
//    @javax.ejb.ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @javax.ejb.ActivationConfigProperty(propertyName = "destinationType", propertyValue = MensajeDefinicion.INTERFACE_QUEUE)})
public class MensajeConsumer implements MessageListener {

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(MensajeConsumer.class.getName());

    @Inject
    private ConverterUtilities converterUtilities;
    @Inject
    private ManejadorColaAlmacenes colaAlmacenes;

    @EJB
    private CierreLoteFacade cierreLoteFacade;

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage mensaje = (ObjectMessage) message;
            LOG.log(Level.INFO, mensaje.toString());
            ObjectMessage msg = (ObjectMessage) message;
            String trama = colaAlmacenes.cierrePinPad((TerminalPinPad) msg.getObject());

            TerminalPinPad pp = (TerminalPinPad) msg.getObject();
            int numMaxGrupo = cierreLoteFacade.findMaxGroupPinpad();
            CierreLote tpc = cierreLoteFacade.findByTidPinpadAndGrupo(pp.getTid(), numMaxGrupo);

            if (trama.contains("AUTORIZADO")) {
                tpc.setEstado(EstadoCierre.CERRADO);
                tpc.setDescripcion("Estado cierre: COMPLETO (cerrado)");
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                tpc.setFechaCierre(dateFormat.format(date));
            }
            if (trama.contains("ERROR")) {
                tpc.setEstado(EstadoCierre.NO_CERRADO);
                tpc.setDescripcion("Estado cierre: COMPLETO (no cerrado)");
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                tpc.setFechaCierre(dateFormat.format(date));
            }
            cierreLoteFacade.edit(tpc);

            LOG.log(Level.INFO, "Trama:------>>>> " + trama);
        } catch (Exception ex) {
            System.out.println("___________________ERROR catch mc");
            Logger.getLogger(MensajeConsumer.class.getName()).log(Priority.FATAL, null, ex);
        }
    }
}
