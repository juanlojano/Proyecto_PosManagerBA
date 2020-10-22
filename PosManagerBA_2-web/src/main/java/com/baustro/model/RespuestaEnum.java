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
public enum RespuestaEnum {

    OK("0000", "TRANSACCION FINALIZADA EXITOSAMENTE"),
    FAIL("0001", "TRANSACCION NO PUDO COMPLETARSE"),
    ERROR("0002", "ERROR EN LA TRANSACCION");

    private String codigoRespuesta;
    private String descripcionRespuesta;

    private RespuestaEnum(String codRespuesta, String descRespuesta) {
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
