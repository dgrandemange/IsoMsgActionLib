/*
 * Created on 20 juil. 07 by dgrandemange
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
 * Data types enumeration
 * 
 * @author dgrandemange
 *
 */
public enum DataType {
    /**
     * UNDEFINED
     */
    UNDEFINED,
    
    /**
     * Regular expression 
     */
    REGEXP,

    /**
     * Date
     */
    DATE,
    
    /**
     * Alpha 
     */
    ALPHA(AllowedCharSetDefinitionMode.REGULAR_EXPRESSION, "A-Za-z"),
    
    /**
     * Numeric 
     */
    NUM(AllowedCharSetDefinitionMode.REGULAR_EXPRESSION, "0-9"),    
    
    /**
     * Space 
     */
    SPACES(AllowedCharSetDefinitionMode.LIST, " "),
    
    /**
     * Special chars 
     */
    SPECIAL(AllowedCharSetDefinitionMode.REGULAR_EXPRESSION, "[\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7F\\x80-\\xFF]*"),
    
    /**
     * Binary
     */
    BINARY    
    ;
        
    enum AllowedCharSetDefinitionMode {
        UNUSED,
        LIST,
        REGULAR_EXPRESSION;
    }
    
    private AllowedCharSetDefinitionMode definitionMode;
    
    private String allowedCharSet;
    
    private DataType() {
        definitionMode = AllowedCharSetDefinitionMode.UNUSED;
        allowedCharSet="";
    }

    /**
     * @param definitionMode
     * @param allowedCharSet
     */
    private DataType(AllowedCharSetDefinitionMode definitionMode, String allowedCharSet) {
        this.definitionMode = definitionMode;
        this.allowedCharSet = allowedCharSet;
    }

    /**
     * @return the allowedCharSet
     */
    public String getAllowedCharSet() {
        return allowedCharSet;
    }

    /**
     * @return the definitionMode
     */
    public AllowedCharSetDefinitionMode getDefinitionMode() {
        return definitionMode;
    }       
    
}
