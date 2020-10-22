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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ue01000632
 */
@Entity
@XmlRootElement
public class Autorizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private TerminalCaja terminalCaja;
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "factura_id", nullable = true)
//    private Factura factura;
    private BigDecimal totalVenta;
    private BigDecimal interes;
    private BigDecimal totalAutoriza;
    private Integer tipoDiferido;
    private Integer plazoDiferido;
    private String codAutorizacion;
    private String idTransaccion;
    private String secuencial;
    private EstadoAutorizacion estadoAutorizacion;
    private EstadoEntity estado;
    private BigDecimal iva;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private int mesesGracia;

    private String codTransaccion;
    private BigDecimal valorServicio;
    private BigDecimal valorPropina;
    private String statusTransaccion;
    private String codigoRespuesta;
    private String descripcionRespuesta;
    private String tipoDispositivo;
    private String codAdquirienteTarjeta;
    private String codAdquirienteServicio;
    private BigDecimal montoBaseGrabaIVA;
    private BigDecimal montoBaseNoGrabaIVA;

    private Long idLote;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = true)
    private Factura factura;

    @ManyToOne
    private Comercio comercio;

//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "lote_id", nullable = true)
//    private Lote lote;
    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTipoDiferido() {
        return tipoDiferido;
    }

    public void setTipoDiferido(Integer tipoDiferido) {
        this.tipoDiferido = tipoDiferido;
    }

    public Integer getPlazoDiferido() {
        return plazoDiferido;
    }

    public void setPlazoDiferido(Integer plazoDiferido) {
        this.plazoDiferido = plazoDiferido;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public TerminalCaja getTerminalCaja() {
        return terminalCaja;
    }

    public void setTerminalCaja(TerminalCaja terminalCaja) {
        this.terminalCaja = terminalCaja;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getTotalAutoriza() {
        return totalAutoriza;
    }

    public void setTotalAutoriza(BigDecimal totalAutoriza) {
        this.totalAutoriza = totalAutoriza;
    }

    public String getCodAutorizacion() {
        return codAutorizacion;
    }

    public void setCodAutorizacion(String codAutorizacion) {
        this.codAutorizacion = codAutorizacion;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public EstadoAutorizacion getEstadoAutorizacion() {
        return estadoAutorizacion;
    }

    public void setEstadoAutorizacion(EstadoAutorizacion estadoAutorizacion) {
        this.estadoAutorizacion = estadoAutorizacion;
    }

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public int getMesesGracia() {
        return mesesGracia;
    }

    public void setMesesGracia(int mesesGracia) {
        this.mesesGracia = mesesGracia;
    }

    public String getCodTransaccion() {
        return codTransaccion;
    }

    public void setCodTransaccion(String codTransaccion) {
        this.codTransaccion = codTransaccion;
    }

    public BigDecimal getValorServicio() {
        return valorServicio;
    }

    public void setValorServicio(BigDecimal valorServicio) {
        this.valorServicio = valorServicio;
    }

    public BigDecimal getValorPropina() {
        return valorPropina;
    }

    public void setValorPropina(BigDecimal valorPropina) {
        this.valorPropina = valorPropina;
    }

    public String getStatusTransaccion() {
        return statusTransaccion;
    }

    public void setStatusTransaccion(String statusTransaccion) {
        this.statusTransaccion = statusTransaccion;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getTipoDispositivo() {
        return tipoDispositivo;
    }

    public void setTipoDispositivo(String tipoDispositivo) {
        this.tipoDispositivo = tipoDispositivo;
    }

    public BigDecimal getMontoBaseGrabaIVA() {
        return montoBaseGrabaIVA;
    }

    public void setMontoBaseGrabaIVA(BigDecimal montoBaseGrabaIVA) {
        this.montoBaseGrabaIVA = montoBaseGrabaIVA;
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
        if (!(object instanceof Autorizacion)) {
            return false;
        }
        Autorizacion other = (Autorizacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.baustro.model.Autorizacion[ id=" + id + " ]";
    }

//    public Lote getLote() {
//        return lote;
//    }
//
//    public void setLote(Lote lote) {
//        this.lote = lote;
//    }
//   
    public Long getIdLote() {
        return idLote;
    }

    public void setIdLote(Long idLote) {
        this.idLote = idLote;
    }

    public String getCodAdquirienteTarjeta() {
        return codAdquirienteTarjeta;
    }

    public void setCodAdquirienteTarjeta(String codAdquirienteTarjeta) {
        this.codAdquirienteTarjeta = codAdquirienteTarjeta;
    }

    public String getCodAdquirienteServicio() {
        return codAdquirienteServicio;
    }

    public void setCodAdquirienteServicio(String codAdquirienteServicio) {
        this.codAdquirienteServicio = codAdquirienteServicio;
    }

    public BigDecimal getMontoBaseNoGrabaIVA() {
        return montoBaseNoGrabaIVA;
    }

    public void setMontoBaseNoGrabaIVA(BigDecimal montoBaseNoGrabaIVA) {
        this.montoBaseNoGrabaIVA = montoBaseNoGrabaIVA;
    }

    public String getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    public void setDescripcionRespuesta(String descripcionRespuesta) {
        this.descripcionRespuesta = descripcionRespuesta;
    }
    
    
    
}
