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
public enum EstadoEntity {
    CREADA("CREADA"),
    MODIFICADA("MODIFICADA"),
    ELIMINADA("ELIMINADO");
    
    
    
    private String estado;
    
    EstadoEntity(String estado) {
        this.estado = estado;
    }
    
    public String estado(){
        return estado;
    }
    
}
