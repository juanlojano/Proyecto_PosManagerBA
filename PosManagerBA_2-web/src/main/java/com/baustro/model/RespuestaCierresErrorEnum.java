/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

/**
 *
 * @author ue01000632
 */
public enum RespuestaCierresErrorEnum {
    ERRORTRAMA("0001", "ERROR EN TRAMA"),
    ERRORPINPAD("0002", "ERROR CONEXIÓN PINPAD"),
    ERRORLOTEVACIO("0006", "LOTE VACIO, LOTE YA ESTA BORRADO"),
    ERRORPROCESO("0020", "ERROR DURANTE PROCESO"),
    ERRORTIMEOUT("00TO", "ERROR TIMEOUT"),
    ERRORCONEXIONPINPAD("00ER", "ERROR CONEXIÓN PINPAD");
    
    private String codigoRespuesta;
    private String descripcionRespuesta;

    private RespuestaCierresErrorEnum(String codRespuesta, String descRespuesta) {
        this.codigoRespuesta = codRespuesta;
        this.descripcionRespuesta = descRespuesta;
    }

    public String getCodigoRespuesta() {
        return this.codigoRespuesta;
    }

    public String getDescripcionRespuesta() {
        return this.descripcionRespuesta;
    }

}
