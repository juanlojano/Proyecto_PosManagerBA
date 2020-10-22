/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.classes;

import com.baustro.jsfclasses.CatalogoErrorController;
import com.baustro.jsfclasses.CatalogoErrorController;
import com.baustro.sessionbean.ErrorFacade;
import com.baustro.model.CatalogoError;
import com.baustro.sessionbean.CatalogoErrorFacade;
import java.io.Serializable;
import javax.ejb.AccessLocalException;
import javax.ejb.ApplicationException;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRequiredException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.NoMoreTimeoutsException;
import javax.ejb.NoSuchEJBException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.TransactionRequiredLocalException;
import javax.ejb.TransactionRolledbackLocalException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ba0100063v
 */
@Named
@RequestScoped
public class CustomException extends Exception implements Serializable {

    private String clase;
    private String superclase;
    private String causa;
    private String codigo;

    @Inject
    private CatalogoErrorController catalogoErrorController;
    
    public String getCodigo() {
    
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getSuperclase() {
        return superclase;
    }

    public void setSuperclase(String superclase) {
        this.superclase = superclase;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    @EJB
    private ErrorFacade ejbErrorFacade;

//    @Inject
//    private CustomException customException;
    private Exception excepcion;

    public CustomException() {
    }

    public CustomException(Exception e) {
        this.excepcion = e;
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getTechExceptionByCodigo(String codError) {
        System.out.println("----------------------------------------------0: " + codError);
        return devolverCustomError(codError).getDescripcionTecnica();
    }

    public String getUserExceptionByCodigo(String codError) {

        return devolverCustomError(codError).getDescripcionUsuario();
    }

    public CatalogoError devolverCustomError(String codError) {
        System.out.println("----------------------------------------------1: " + codError);
        CatalogoError catalogoError = ejbErrorFacade.FindErrorByCodigo(codError);
        System.out.println("----------------------------------------------2: " + catalogoError);

        return catalogoError;
    }

    public String getError(Throwable cause) {

        System.out.println("ERROR: " + cause);

        return null;
    }

    public CustomException retornarCustomException() {
        System.out.println("________________ : " + excepcion);
        CustomException obCustomEx = new CustomException();
        if (excepcion != null
                && excepcion.getClass().getSuperclass().getSimpleName() != null
                && excepcion.getClass().getSimpleName() != null
                && excepcion.getCause() != null) {
            if (excepcion.getClass().getSuperclass().getSimpleName().equals("RuntimeException")) {
                System.out.println("----------------RuntimeException");
                if (excepcion instanceof AccessLocalException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExALEx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof ConcurrentAccessException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExCAEx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof EJBAccessException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExEJBAEx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof EJBException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExEJBEx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof EJBTransactionRequiredException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExEJBTREx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof EJBTransactionRolledbackException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExEJBTRbEx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof NoMoreTimeoutsException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExNoMTE00001");

                    return obCustomEx;
                }

                if (excepcion instanceof NoSuchEJBException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExNoSEJBEx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof NoSuchEntityException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExNoSEEx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof NoSuchObjectLocalException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExNoSOLEx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof TransactionRequiredLocalException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExTRLEx00001");

                    return obCustomEx;
                }

                if (excepcion instanceof TransactionRolledbackLocalException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "RExTRLEx00002");

                    return obCustomEx;
                }

                if (excepcion instanceof PersistenceException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "PEx00001");

                    return obCustomEx;
                }
            }

            if (excepcion.getClass().getSuperclass().getSimpleName().equals("FileNotFoundException")) {
                obCustomEx = setearObjeto(obCustomEx, excepcion, "FNFEx00001");

                return obCustomEx;
            }

            if (excepcion.getClass().getSuperclass().getSimpleName().equals("IOException")) {
                obCustomEx = setearObjeto(obCustomEx, excepcion, "IOEx00001");

                return obCustomEx;
            }

            if (excepcion.getClass().getSuperclass().getSimpleName().equals("ClassNotFoundException")) {
                obCustomEx = setearObjeto(obCustomEx, excepcion, "CNFEx00001");

                return obCustomEx;
            }

            if (excepcion.getClass().getSuperclass().getSimpleName().equals("EOFException")) {
                obCustomEx = setearObjeto(obCustomEx, excepcion, "EOFEx00001");

                return obCustomEx;
            }

            if (excepcion.getClass().getSuperclass().getSimpleName().equals("ArrayIndexOutOfBoundsException")) {
                obCustomEx = setearObjeto(obCustomEx, excepcion, "AIOBEx00001");

                return obCustomEx;
            }

            if (excepcion.getClass().getSuperclass().getSimpleName().equals("NumberFormatException")) {
                obCustomEx = setearObjeto(obCustomEx, excepcion, "NFEx00001");

                return obCustomEx;
            }

            if (excepcion.getClass().getSuperclass().getSimpleName().equals("NullPointerException")) {
                obCustomEx = setearObjeto(obCustomEx, excepcion, "NPEx00001");

                return obCustomEx;
            }

            if (excepcion.getClass().getSuperclass().getSimpleName().equals("SocketException")) {
                obCustomEx = setearObjeto(obCustomEx, excepcion, "NPEx00001");

                return obCustomEx;
            }
            
            if (excepcion.getClass().getSuperclass().getSimpleName().equals("Exception")) {
                if (excepcion instanceof JRException) {
                    obCustomEx = setearObjeto(obCustomEx, excepcion, "JREx00001");

                    return obCustomEx;
                }
            }
        } else {
//            obCustomEx.setCodigo("ExGeneral404");
//            obCustomEx.setSuperclase(excepcion.toString());
//            obCustomEx.setClase(excepcion.toString());
//            obCustomEx.setCausa(excepcion.toString());
            
            catalogoErrorController= new CatalogoErrorController();

            CatalogoErrorController cec= new CatalogoErrorController();
            CatalogoError catalogo= new CatalogoError();
            catalogo.setCodError(excepcion.toString());
            catalogo.setDescripcionTecnica(excepcion.toString());
            catalogo.setDescripcionUsuario(excepcion.toString());
            System.out.println("____________:  " + catalogoErrorController);
            catalogoErrorController.create(catalogo); 
        }

        return obCustomEx;
    }

    private CustomException setearObjeto(CustomException objCustomException, Exception exception, String codigoError) {
        objCustomException.setSuperclase(exception.getClass().getSuperclass().getSimpleName());
        objCustomException.setClase(exception.getClass().getSimpleName());
        objCustomException.setCausa(exception.getCause().getMessage()+ "-->detalle: "+ exception);
        objCustomException.setCodigo(codigoError);

        return objCustomException;
    }
}
