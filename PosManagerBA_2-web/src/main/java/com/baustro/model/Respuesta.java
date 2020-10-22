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
public class Respuesta<T> {
    private String codigoRespuesta;
    private String descripcionRespuesta;
    private T objetoRespuesta;

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    public void setDescripcionRespuesta(String descripcionRespuesta) {
        this.descripcionRespuesta = descripcionRespuesta;
    }

    public T getObjetoRespuesta() {
        return objetoRespuesta;
    }

    public void setObjetoRespuesta(T objetoRespuesta) {
        this.objetoRespuesta = objetoRespuesta;
    }

   
    
    
    
    
}
