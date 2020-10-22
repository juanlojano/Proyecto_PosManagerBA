/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author ue01000632
 */
@Stateless
@Entity
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = true, length = 1000)
    private String tramaRespuesta;
    @Column(nullable = true, length = 1000)
    private String tramaEnvio;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autorizacion_id", nullable = true)
    private Autorizacion autorizacion;
    private EstadoEntity estado;
    private String secuencial;
    @Column(nullable = true, length = 1000)
    private String tramaAnulacion;
    @Column(nullable = true, length = 1000)
    private String tramaReverso;

    private String numeroTarjeta;
    private String adquirienteTarjeta;
    private String adquirienteServicio;

    public String getTramaAnulacion() {
        return tramaAnulacion;
    }

    public void setTramaAnulacion(String tramaAnulacion) {
        this.tramaAnulacion = tramaAnulacion;
    }

    public String getTramaReverso() {
        return tramaReverso;
    }

    public void setTramaReverso(String tramaReverso) {
        this.tramaReverso = tramaReverso;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public String getTramaRespuesta() {
        return tramaRespuesta;
    }

    public void setTramaRespuesta(String tramaRespuesta) {
        this.tramaRespuesta = tramaRespuesta;
    }

    public String getTramaEnvio() {
        return tramaEnvio;
    }

    public void setTramaEnvio(String tramaEnvio) {
        this.tramaEnvio = tramaEnvio;
    }

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }

    public Autorizacion getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(Autorizacion autorizacion) {
        this.autorizacion = autorizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getAdquirienteTarjeta() {
        return adquirienteTarjeta;
    }

    public void setAdquirienteTarjeta(String adquirienteTarjeta) {
        this.adquirienteTarjeta = adquirienteTarjeta;
    }

    public String getAdquirienteServicio() {
        return adquirienteServicio;
    }

    public void setAdquirienteServicio(String adquirienteServicio) {
        this.adquirienteServicio = adquirienteServicio;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Voucher)) {
            return false;
        }
        Voucher other = (Voucher) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.baustro.model.Voucher[ id=" + id + " ]";
    }

}
