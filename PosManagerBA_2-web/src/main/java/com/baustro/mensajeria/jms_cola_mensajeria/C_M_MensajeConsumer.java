package com.baustro.mensajeria.jms_cola_mensajeria;

import com.baustro.bean.almacenes.ManejadorColaAlmacenes;
import com.baustro.model.Autorizacion;
import com.baustro.model.AutorizacionAuxiliar;
import com.baustro.model.Voucher;
import com.baustro.sessionbean.ComercioFacade;
import com.baustro.sessionbean.TerminalCajaFacade;
import com.baustro.sessionbean.VoucherFacade;
import com.baustro.utility.ConverterUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Stateless
public class C_M_MensajeConsumer {

    @Inject
    private ManejadorColaAlmacenes procesamientoTRAMA;

    @Inject
    private VoucherFacade voucherFacade;

    @Inject
    private TerminalCajaFacade terminalCajaFacade;
    @Inject
    private ComercioFacade comercioFacade;
    @Inject
    private ConverterUtilities converter;

    public String onMessage() throws IOException, JMSException {
        TextMessage txtMessage = null;
        Session session = null;
        Connection connection=null;
        MessageConsumer consumer = null;
        String respuesta=null;
        try {
            Context jndiContext = new InitialContext();
            ConnectionFactory conectionFactory = (ConnectionFactory) jndiContext.lookup("java:/jms/PosManagerConnectionFactory");
            Destination queue = (Destination) jndiContext.lookup("java:/jms/queue/colaMensajeriaPinPad");

            connection = conectionFactory.createConnection("admin0", "admin0");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(queue);
            connection.start();
            Message mensajeReceive = consumer.receive(1000);

            txtMessage = (TextMessage) mensajeReceive;
            
            System.out.println("Recibido sincrono [" + txtMessage.getText() + "]");
            
            String[] object = txtMessage.getText().split("/");
            ObjectMapper mapper = new ObjectMapper();
            AutorizacionAuxiliar autorizacionauxiliar = mapper.readValue(object[0], AutorizacionAuxiliar.class);

            Autorizacion entity = converter.castToAutorizacion(autorizacionauxiliar);
            entity.setComercio(comercioFacade.findComercioByCodigoComercioBA(entity.getComercio().getId()));
            entity.setTerminalCaja(terminalCajaFacade.findTerminalCajaByCodTerminal(entity.getTerminalCaja().getId()));
            
            respuesta = procesamientoTRAMA.EnviarTramaPago(entity, object[1]);
            
            System.out.println("Fin sincrono");

        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            // Cerramos los recursos
            consumer.close();
            session.close();
            connection.close();
        }

        return respuesta;

    }

}
