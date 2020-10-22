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
public enum EstadoLote {
    ABIERTO("LOTE ABIERTO"),
    CERRADO("LOTE CERRADO"),
    INCONSISTENTE("LOTE INCONSISTENTE"),
    ELIMINADO("LOTE ELIMINADO");
    
    
    
    
    private String estado;
    
    EstadoLote(String estado) {
        this.estado = estado;
    }
    
    public String estado(){
        return estado;
    }
}
