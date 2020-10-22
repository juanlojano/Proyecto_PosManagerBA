/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ue01000632
 */
@Entity
//@ConversationScoped
@XmlRootElement
public class BinTarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)

    private String descripcion;

    private String numero;

//    @Enumerated(EnumType.STRING)
    private EstadoEntity estado;

//    @ManyToMany(targetEntity = TipoPago.class, cascade = {CascadeType.ALL})
//    @JoinTable(
//            name = "binTarjeta_tipoPago",
//            joinColumns = @JoinColumn(name = "binTarjeta_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "tipoPago_id", referencedColumnName = "id"))
//    private List<TipoPago> tiposPago;
    
    
//    @OneToMany(mappedBy = "binTarjeta")
//    private List<TipoPago> tiposPago;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipoPago_id", nullable = true)
    private TipoPago tipoPago;


    
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

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
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
        if (!(object instanceof Comercio)) {
            return false;
        }
        BinTarjeta other = (BinTarjeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}