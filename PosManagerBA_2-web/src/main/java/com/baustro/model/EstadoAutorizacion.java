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
public enum EstadoAutorizacion {
    NORMAL("AUTORIZACION NORMAL"),
    ANULADA("AUTORIZACION ANULADA"),
    REVERSO("AUTORIZACION REVERSADA"),
    INCONSISTENTE("AUTORIZACION INCONSISTENTE"),
    REVERSO_AUTOMATICO("REALIZADO REVERSO AUTOMATICO");

    private String estado;

    EstadoAutorizacion(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

}
