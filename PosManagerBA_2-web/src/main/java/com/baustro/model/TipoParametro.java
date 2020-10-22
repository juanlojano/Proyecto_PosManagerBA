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
public enum TipoParametro {
    TIPO_DIFERIDO(1),
    PLAZO_DIFERIDO(2);
    
    private final int value;

    private TipoParametro(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
