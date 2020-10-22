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
public class FacturaAuxiliar {
    
    private Date fecha;
    private String codFactura;    
    private List<BaseConsumo> consumos;
    private BigDecimal subTotal;    
    private List<Impuesto> impuestos;
    private BigDecimal iva;
    private BigDecimal total;
    private List<Autorizacion> autorizaciones;

    public List<Autorizacion> getAutorizaciones() {
        return autorizaciones;
    }

    public void setAutorizaciones(List<Autorizacion> autorizaciones) {
        this.autorizaciones = autorizaciones;
    }    
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(String codFactura) {
        this.codFactura = codFactura;
    }

    public List<BaseConsumo> getConsumos() {
        return consumos;
    }

    public void setConsumos(List<BaseConsumo> consumos) {
        this.consumos = consumos;
    }

   
    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public List<Impuesto> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<Impuesto> impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    
    
}
