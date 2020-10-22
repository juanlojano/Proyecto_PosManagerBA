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
public enum EstadoCierre {
    INICIADO("INICIADO"),
    CERRANDO("CERRANDO"),
    CERRADO("CERRADO"),
    NO_CERRADO("NO_CERRADO");
    

    private String estado;

    EstadoCierre(String estado) {
        this.estado = estado;
    }

    public String estado() {
        return estado;
    }

}
