package org.jpos.jposext.isomsgaction.model.validation;

/**
 * Availables validation error types
 * 
 * @author dgrandemange
 *
 */
public enum ValidationErrorTypeEnum {
	
    /**
     * Error related to field presence check
     */
    FIELD_PRESENCE,
    
    /**
     * Current data lenth is invalid, or does not match a speicified fixed length, or exceeeds a specified max length
     */
    INVALID_LENGTH,
    
    /**
     * Current data type is not the expected one
     */
    INVALID_TYPE;
    
}
