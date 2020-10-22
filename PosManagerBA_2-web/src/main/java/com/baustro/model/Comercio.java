/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ue01000632
 */
@Entity
//@ConversationScoped
@XmlRootElement
public class Comercio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)

    private String codComercio;
    private String codComercioBa;
    private String nombre;
    private String ciudad;
//    
//    @Enumerated(EnumType.STRING)
    private EstadoEntity estado;
    private String ruc;

    @OneToMany(mappedBy = "comercio")
    private List<Autorizacion> autorizaciones;

    @OneToMany(mappedBy = "comercio")
    private List<TerminalCaja> cajas;

    @OneToMany(mappedBy = "comercio")
    private List<TerminalPinPad> pinpads;

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodComercio() {
        return codComercio;
    }

    public void setCodComercio(String codComercio) {
        this.codComercio = codComercio;
    }

    public String getCodComercioBa() {
        return codComercioBa;
    }

    public void setCodComercioBa(String codComercioBa) {
        this.codComercioBa = codComercioBa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Autorizacion> getAutorizaciones() {
        return autorizaciones;
    }

    public void setAutorizaciones(List<Autorizacion> autorizaciones) {
        this.autorizaciones = autorizaciones;
    }

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @XmlTransient
    public List<TerminalCaja> getCajas() {
        return cajas;
    }

    public void setCajas(List<TerminalCaja> cajas) {
        this.cajas = cajas;
    }

    @XmlTransient
    public List<TerminalPinPad> getPinpads() {
        return pinpads;
    }

    public void setPinpads(List<TerminalPinPad> pinpads) {
        this.pinpads = pinpads;
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
        Comercio other = (Comercio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.baustro.model.Comercio[ id=" + id + " ]";
    }

}
