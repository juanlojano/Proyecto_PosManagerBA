/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ue01000632
 */
public class AutorizacionAuxiliar {

    private String TipoTransaccion;
    private TerminalCaja terminalCaja;
    private Comercio comercio;
    private BigDecimal totalVenta;
    private BigDecimal interes;
    private BigDecimal totalAutoriza;
    private Integer tipoDiferido;
    private Integer plazoDiferido;
    private String codAutorizacion;
    private String secuencial;
    private List<BaseConsumo> consumos;
    private BigDecimal iva;
    private String idTransaccion;
    private int mesesGracia;
    private String hora;
    private String fecha;
    private BigDecimal montoBaseGrabaIVA;
    private BigDecimal montoBaseNoGrabaIVA;
    private BigDecimal valorServicio;
    private BigDecimal valorPropina;

    public BigDecimal getMontoBaseGrabaIVA() {
        return montoBaseGrabaIVA;
    }

    public void setMontoBaseGrabaIVA(BigDecimal montoBaseGrabaIVA) {
        this.montoBaseGrabaIVA = montoBaseGrabaIVA;
    }

    public BigDecimal getMontoBaseNoGrabaIVA() {
        return montoBaseNoGrabaIVA;
    }

    public void setMontoBaseNoGrabaIVA(BigDecimal montoBaseNoGrabaIVA) {
        this.montoBaseNoGrabaIVA = montoBaseNoGrabaIVA;
    }
    
    public String getTipoTransaccion() {
        return TipoTransaccion;
    }

    public void setTipoTransaccion(String TipoTransaccion) {
        this.TipoTransaccion = TipoTransaccion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    

    public int getMesesGracia() {
        return mesesGracia;
    }

    public void setMesesGracia(int mesesGracia) {
        this.mesesGracia = mesesGracia;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public TerminalCaja getTerminalCaja() {
        return terminalCaja;
    }

    public void setTerminalCaja(TerminalCaja terminalCaja) {
        this.terminalCaja = terminalCaja;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
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

    public List<BaseConsumo> getConsumos() {
        return consumos;
    }

    public void setConsumos(List<BaseConsumo> consumos) {
        this.consumos = consumos;
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
    
}
