/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ue01000632
 */
@Entity
@XmlRootElement
public class Lote implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer numeroLote;
    private Integer numTotalTrans;
    private Integer numTransOk;
    private Integer numTransInconsistentes;
    private Double valorTotalTrans;

    private BigDecimal valor;
    private EstadoEntity estado;
    private EstadoLote estadoLote;
    private Date fechaLote;

    @ManyToOne
    private TerminalPinPad pinpad;

    public Date getFechaLote() {
        return fechaLote;
    }

    public void setFechaLote(Date fechaLote) {
        this.fechaLote = fechaLote;
    }
    
    public EstadoLote getEstadoLote() {
        return estadoLote;
    }

    public void setEstadoLote(EstadoLote estadoLote) {
        this.estadoLote = estadoLote;
    }

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(Integer numeroLote) {
        this.numeroLote = numeroLote;
    }

    public TerminalPinPad getPinpad() {
        return pinpad;
    }

    public void setPinpad(TerminalPinPad pinpad) {
        this.pinpad = pinpad;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
        if (!(object instanceof Lote)) {
            return false;
        }
        Lote other = (Lote) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.baustro.model.Lote[ id=" + id + " ]";
    }

    public Integer getNumTotalTrans() {
        return numTotalTrans;
    }

    public void setNumTotalTrans(Integer numTotalTrans) {
        this.numTotalTrans = numTotalTrans;
    }

    public Integer getNumTransOk() {
        return numTransOk;
    }

    public void setNumTransOk(Integer numTransOk) {
        this.numTransOk = numTransOk;
    }

    public Integer getNumTransInconsistentes() {
        return numTransInconsistentes;
    }

    public void setNumTransInconsistentes(Integer numTransInconsistentes) {
        this.numTransInconsistentes = numTransInconsistentes;
    }

    public Double getValorTotalTrans() {
        return valorTotalTrans;
    }

    public void setValorTotalTrans(Double valorTotalTrans) {
        this.valorTotalTrans = valorTotalTrans;
    }


}
