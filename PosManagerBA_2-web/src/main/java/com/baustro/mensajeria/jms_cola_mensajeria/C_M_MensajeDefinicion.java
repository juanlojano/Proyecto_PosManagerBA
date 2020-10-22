package com.baustro.mensajeria.jms_cola_mensajeria;

import javax.jms.JMSDestinationDefinition;

@JMSDestinationDefinition(name = C_M_MensajeDefinicion.MENSAJE_QUEUE, interfaceName = C_M_MensajeDefinicion.INTERFACE_QUEUE)
public class C_M_MensajeDefinicion {

    public static final String MENSAJE_QUEUE = "java:/jms/queue/colaMensajeriaPinPad";
    public static final String INTERFACE_QUEUE = "javax.jms.Queue";

}
