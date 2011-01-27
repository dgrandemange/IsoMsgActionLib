/*
 * Created on 17 juil. 07 by dgrandemange
 *
 * Copyright (c) 2005 Setib
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Setib ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Setib.
 */
package org.jpos.jposext.isomsgaction.model.validation;


/**
 * Validation error description class 
 * 
 * @author dgrandemange
 *
 */
public class ValidationError {
    
    /**
     * Type de l'erreur de validation 
     */
    private ValidationErrorTypeEnum typeErreur;
    
    /**
     * Identifiant paramètre en erreur
     */
    private String paramName;

    public ValidationError() {
    	super();
    }

    /**
     * @param typeErreur
     * @param paramName
     */
    public ValidationError(ValidationErrorTypeEnum typeErreur, String paramName) {
	super();
	this.typeErreur = typeErreur;
	this.paramName = paramName;
    }    
    
    /**
     * @param information
     * @param typeErreur
     */
    public ValidationError(ValidationErrorTypeEnum typeErreur) {
        this.typeErreur = typeErreur;
    }

    /**
     * @return the typeErreur
     */
    public ValidationErrorTypeEnum getTypeErreur() {
        return typeErreur;
    }

    /**
     * @param typeErreur the typeErreur to set
     */
    public void setTypeErreur(ValidationErrorTypeEnum typeErreur) {
        this.typeErreur = typeErreur;
    }

    /**
     * @return the paramName
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * @param paramName the paramName to set
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

	@Override
	public String toString() {
		return super.toString();
	}
        
}
