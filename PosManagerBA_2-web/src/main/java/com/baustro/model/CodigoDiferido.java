/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author ue01000632
 */
public enum CodigoDiferido {
    NORMAL("00"),
    HOMOLOGADO("01");
    
    private String codigoDiferido;
    
    CodigoDiferido(String codigoDiferido) {
        this.codigoDiferido = codigoDiferido;
    }

    public String getCodigoDiferido() {
        return codigoDiferido;
    }

    public void setCodigoDiferido(String codigoDiferido) {
        this.codigoDiferido = codigoDiferido;
    }
    
    
    
}
