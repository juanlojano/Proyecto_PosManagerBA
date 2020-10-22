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
public enum Tipo{
    CREDITO("CC"),
    DEBITO("DD");
    private String tipo;
    
    Tipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String tipo(){
        return tipo;
    }
}
