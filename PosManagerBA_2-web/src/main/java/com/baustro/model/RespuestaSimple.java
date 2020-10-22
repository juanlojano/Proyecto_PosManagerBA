/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

/**
 *
 * @author ue01000632
 * @param <T>
 */
public class RespuestaSimple<T> {
    private T objetoRespuesta;


    public T getObjetoRespuesta() {
        return objetoRespuesta;
    }
    
    public void setObjetoRespuesta(T objetoRespuesta) {
        this.objetoRespuesta = objetoRespuesta;
    }
}
