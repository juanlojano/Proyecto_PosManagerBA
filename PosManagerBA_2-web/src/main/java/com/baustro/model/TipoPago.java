/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ue01000632
 */
@Entity
//@ConversationScoped
@XmlRootElement
public class TipoPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)

    private String descripcion;

//    @Enumerated(EnumType.STRING)
    private EstadoEntity estado;

    @ManyToOne
    private Institucion institucion;

    @ManyToOne
    private Plazo plazo;

//    @ManyToOne
//    private BinTarjeta binTarjeta;
    @ManyToOne
    private TipoDiferido tipoDiferido;

//    @ManyToMany(mappedBy = "tiposPago", cascade = {CascadeType.ALL}, targetEntity = BinTarjeta.class)
//    private List<BinTarjeta> binesTarjeta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Plazo getPlazo() {
        return plazo;
    }

    public void setPlazo(Plazo plazo) {
        this.plazo = plazo;
    }

    public TipoDiferido getTipoDiferido() {
        return tipoDiferido;
    }

    public void setTipoDiferido(TipoDiferido tipoDiferido) {
        this.tipoDiferido = tipoDiferido;
    }

    @XmlTransient
    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }

//    @XmlTransient
//    public List<BinTarjeta> getBinesTarjeta() {
//        return binesTarjeta;
//    }
//
//    public void setBinesTarjeta(List<BinTarjeta> binesTarjeta) {
//        this.binesTarjeta = binesTarjeta;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comercio)) {
            return false;
        }
        TipoPago other = (TipoPago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
