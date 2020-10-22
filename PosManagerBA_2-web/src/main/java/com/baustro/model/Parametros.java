/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ba0100063v
 */
@Entity
@XmlRootElement
public class Parametros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    
    @Basic(optional = false)
    @Column(name = "param_id")
    private Integer paramId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "param_codigo")
    private int paramCodigo;
    @Size(max = 50)
    @Column(name = "param_valor1")
    private String paramValor1;
    @Size(max = 50)
    @Column(name = "param_valor2")
    private String paramValor2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "param_descripcion")
    private String paramDescripcion;

    public Parametros() {
    }

    public Parametros(Integer paramId) {
        this.paramId = paramId;
    }

    public Parametros(Integer paramId, int paramCodigo, String paramDescripcion) {
        this.paramId = paramId;
        this.paramCodigo = paramCodigo;
        this.paramDescripcion = paramDescripcion;
    }

    public Integer getParamId() {
        return paramId;
    }

    public void setParamId(Integer paramId) {
        this.paramId = paramId;
    }

    public int getParamCodigo() {
        return paramCodigo;
    }

    public void setParamCodigo(int paramCodigo) {
        this.paramCodigo = paramCodigo;
    }

    public String getParamValor1() {
        return paramValor1;
    }

    public void setParamValor1(String paramValor1) {
        this.paramValor1 = paramValor1;
    }

    public String getParamValor2() {
        return paramValor2;
    }

    public void setParamValor2(String paramValor2) {
        this.paramValor2 = paramValor2;
    }

    public String getParamDescripcion() {
        return paramDescripcion;
    }

    public void setParamDescripcion(String paramDescripcion) {
        this.paramDescripcion = paramDescripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paramId != null ? paramId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parametros)) {
            return false;
        }
        Parametros other = (Parametros) object;
        if ((this.paramId == null && other.paramId != null) || (this.paramId != null && !this.paramId.equals(other.paramId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.baustro.model.Parametros[ paramId=" + paramId + " ]";
    }
    
}
