/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.utility;

import com.baustro.classes.CustomException;
import com.baustro.classes.VoucherClass;
import com.baustro.model.Autorizacion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author ue0100063v
 */
@Singleton
@Startup
@Lock(LockType.READ) //to allow simultaneous invocation 
@AccessTimeout(value = 60, unit = TimeUnit.SECONDS)
public class Reporte {

    public void reporteAutorizacion(Autorizacion objAutorizacion, VoucherClass objVoucher) throws JRException, FileNotFoundException {
//        for (BaseConsumo baseConsumo : objetoAutorizacion.getFactura().getBaseConsumos()) {
//            if (baseConsumo.getTarifa().compareTo("14") == 0) {
//                parametros.put("baseConsumo14", baseConsumo.getValor());
//            } else {
//                parametros.put("baseConsumo14", "0");
//            }
//            if (baseConsumo.getTarifa().compareTo("0") == 0) {
//                parametros.put("baseConsumo0", baseConsumo.getValor());
//            } else {
//                parametros.put("baseConsumo0", "0");
//            }
//        }
        try {
            Map parametros = new HashMap();
            System.out.println("hola....");
            System.out.println(objAutorizacion.getTotalVenta().toString());

            parametros.put("codAutorizacion", objVoucher.getNumeroAutorizacion());
            parametros.put("baseConsumo14", "0.00");
            parametros.put("baseConsumo0", "0.00");
            parametros.put("subtotal", objAutorizacion.getFactura().getSubTotal().toString());
            parametros.put("iva", objAutorizacion.getFactura().getIva().toString());
            parametros.put("total", objAutorizacion.getTotalVenta().toString());
            parametros.put("fechaHoraActual", objAutorizacion.getFactura().getFecha().toString());
            parametros.put("titular", objVoucher.getNombreTarjetaHabiente());
            parametros.put("telefono", "________________");
            parametros.put("ci", "ci");
            parametros.put("visa", objVoucher.getNombreGrupoTarjeta());
            parametros.put("ruc", "0000");
            parametros.put("codigo", "00");
            parametros.put("lote", "000");
            parametros.put("terminal", objVoucher.getTerminalId());
            parametros.put("referencia", "referencia");
            parametros.put("preferedName", objVoucher.getIdentificacionEMV());
            parametros.put("mid", objVoucher.getMerchantId());
            parametros.put("adq", objVoucher.getCodigoRedAdquirente());
            parametros.put("emv_tsi", objVoucher.getTsi());
            parametros.put("emv_tvr", objVoucher.getTvr());
            parametros.put("emv_aid", objVoucher.getAid_emv());
            parametros.put("parameterprueba", "PARAMPRUEBA");

            Properties properties = new Properties();
            properties.load(new FileInputStream(System.getProperty("jboss.server.config.dir") + "/propertiesPosmanager.properties"));
            String reportLocation = properties.getProperty("reportLocation");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportLocation);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
//            JasperExportManager.exportReportToPdfFile(jasperPrint, "D://" + objVoucher.getSecuencialTransaccion() + ".pdf");

            Properties propertiesPDF = new Properties();
            propertiesPDF.load(new FileInputStream(System.getProperty("jboss.server.config.dir") + "/propertiesPosmanager.properties"));
            String reportLocationPDF = propertiesPDF.getProperty("reportLocationPDF");
            File file = new File(System.getProperty("user.dir") + "\\reporteAutorizacion.pdf");
            String filePath1 = reportLocationPDF + File.separator + "reporteAutorizacion.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, filePath1);
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("----------------------------------------CATCH");
            System.out.println("_______________________________________ " + e);
            System.out.println("_______________________________________ " + e.getMessage());
            CustomException customException = new CustomException(e);
            customException = customException.retornarCustomException();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoCustomException", customException);
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pretty:errorPage");
        } finally {
            System.out.println("-------------------FINALLY");
        }
    }

    public String obtenerFechaActual() {
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String fecha = hourdateFormat.format(date);
        return fecha;
    }

}
