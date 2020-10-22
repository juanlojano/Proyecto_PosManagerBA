package com.baustro.mensajeria.jms;

import javax.jms.JMSDestinationDefinition;

@JMSDestinationDefinition(name = MensajeDefinicion.MENSAJE_QUEUE, interfaceName = MensajeDefinicion.INTERFACE_QUEUE)
public class MensajeDefinicion {

    public static final String MENSAJE_QUEUE = "java:/jms/queue/mensajeriaPinPad";
    public static final String INTERFACE_QUEUE = "javax.jms.Queue";

}
