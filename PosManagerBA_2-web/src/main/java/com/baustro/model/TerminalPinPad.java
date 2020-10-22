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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author ue01000632
 */
@Entity
@Table(name = "TerminalPinPad")
@XmlRootElement
public class TerminalPinPad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    @Column(name = "id")
    private Long id;

    private String tid;

    private String ip;

    private int puerto;

    @OneToMany(mappedBy = "pinpadPrincipal", fetch = FetchType.EAGER)
    private List<TerminalCaja> cajas;

    @ManyToOne
    private Comercio comercio;

//    @OneToMany(mappedBy = "pinpad")
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pinpad", cascade = CascadeType.ALL)
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pinpad", cascade = CascadeType.ALL)
//    @OneToMany(mappedBy = "pinpad", orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Lote> lote;

    private EstadoEntity estado;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @XmlTransient
    public List<TerminalCaja> getCajas() {
        return cajas;
    }

    public void setCajas(List<TerminalCaja> cajas) {
        this.cajas = cajas;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    @XmlTransient
    public List<Lote> getLote() {
        return lote;
    }

    public void setLote(List<Lote> lote) {
        this.lote = lote;
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
        if (!(object instanceof TerminalPinPad)) {
            return false;
        }
        TerminalPinPad other = (TerminalPinPad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TerminalPinPad{" + "id=" + id + ", tid=" + tid + ", ip=" + ip + ", puerto=" + puerto + ", cajas=" + cajas + ", comercio=" + comercio + ", lote=" + lote + ", estado=" + estado + '}';
    }

}
