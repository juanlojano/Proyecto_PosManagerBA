package com.baustro.mensajeria.jms_cola_mensajeria;

import com.baustro.bean.almacenes.ManejadorColaAlmacenes;
import com.baustro.model.Autorizacion;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Dependent
public class C_M_MensajeProducer {

    @EJB
    private ManejadorColaAlmacenes manejadorColaAlmacenesEJB;

    public void sendMensaje(String stringMessage) throws JMSException, NamingException, IOException {

        try {
            Context jndiContext = new InitialContext();
            ConnectionFactory conectionFactory = (ConnectionFactory) jndiContext.lookup("java:/jms/PosManagerConnectionFactory");
            Destination queue = (Destination) jndiContext.lookup("java:/jms/queue/colaMensajeriaPinPad");

            Connection connection = conectionFactory.createConnection("admin0", "admin0");
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer= session.createProducer(queue);
            TextMessage txtMessage= session.createTextMessage(stringMessage);
            producer.send(txtMessage);
            
            connection.close();

        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

    public String sendMensaje(Autorizacion autorizacion, String tramaEnvio) {
        String respuesta = manejadorColaAlmacenesEJB.EnviarTramaPago(autorizacion, tramaEnvio);

        return respuesta;
    }

}
