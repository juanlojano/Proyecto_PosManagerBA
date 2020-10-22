/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author ba0100063v
 */

@FacesValidator
public class NoBlankSpaceValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        //Check if user has typed only blank spaces
        if (value.toString().trim().isEmpty()) {
            FacesMessage msg = new FacesMessage("Error en el ingreso de datos", "Los campos no puede ser llenados unicamente de espacios en blanco");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, msg);
            throw new ValidatorException(msg);
        }
    }

}
