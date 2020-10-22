package com.baustro.mensajeria.jms;

import javax.enterprise.context.ApplicationScoped;
import javax.jms.CompletionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

@ApplicationScoped
public class MensajeListener implements CompletionListener {

    private String MessageID;

    @Override
    public void onCompletion(Message message) {
        try {
            Object[] params = {message.getJMSMessageID(), message.getJMSCorrelationID()};
            MessageID = message.getJMSMessageID();
            Logger.getLogger(MensajeListener.class).info("Mensaje enviado correctamente, " + message.getJMSMessageID());
        } catch (JMSException e) {
            Logger.getLogger(MensajeListener.class).fatal("Hubo algun problema con el formato del mensaje: " + e.getMessage());
            
        } 
    }

    @Override
    public void onException(Message message, Exception exception) {
        try {
            String messageID = message.getJMSMessageID();
            Logger.getLogger(MensajeListener.class).log(Priority.INFO, "Envio fallido... : " + messageID + ". La exepcion es: " + exception.getMessage()); // info("Envio fallido... : " + messageID + ". La exepcion es: " + exception.getMessage());
        } catch (JMSException e) {
            Logger.getLogger(MensajeListener.class).fatal("Hubo algun problema con el formato del mensaje: " + e.getMessage());
        }
    }

    public String getMessageID() {
        return MessageID;
    }

    public void setMessageID(String MessageID) {
        this.MessageID = MessageID;
    }

}
