/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ue01000632
 */
@Entity
@XmlRootElement
public class TerminalCaja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     
//    @GeneratedValue(strategy = GenerationType.AUTO)     
    
    private Long id;    
    
    private String codTerminal;    
    
    private String ip;
    
    private String codOficina;    
    
    @ManyToOne
    private Comercio comercio;    
    
    @ManyToOne(fetch = FetchType.EAGER)
    private TerminalPinPad pinpadPrincipal;    
    
    @OneToMany(mappedBy = "terminalCaja",fetch = FetchType.EAGER)
    private List<Autorizacion> autorizaciones;
    
    private EstadoEntity estado;

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

    public String getCodTerminal() {
        return codTerminal;
    }

    public void setCodTerminal(String codTerminal) {
        this.codTerminal = codTerminal;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCodOficina() {
        return codOficina;
    }

    public void setCodOficina(String codOficina) {
        this.codOficina = codOficina;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public TerminalPinPad getPinpadPrincipal() {
        return pinpadPrincipal;
    }

    public void setPinpadPrincipal(TerminalPinPad pinpadPrincipal) {
        this.pinpadPrincipal = pinpadPrincipal;
    }

    @XmlTransient
    public List<Autorizacion> getAutorizaciones() {
        return autorizaciones;
    }

    public void setAutorizaciones(List<Autorizacion> autorizaciones) {
        this.autorizaciones = autorizaciones;
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
        if (!(object instanceof TerminalCaja)) {
            return false;
        }
        TerminalCaja other = (TerminalCaja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.baustro.model.TerminalCaja[ id=" + id + " ]";
    }

   
    
}
