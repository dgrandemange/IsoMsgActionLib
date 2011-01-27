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

import java.util.List;

/**
 * Syntaxic rule definition class
 * 
 * @author dgrandemange
 *
 */
public class ValidationRule {

    /**
     * Name of the information to validate
     */
    private String name;

    /**
     * Length: 
     * <BR>if fixed length, indicates the expected fixed length
     * <BR>else indicates max allowed length
     */
    private int length;

    /**
     * Variable length indicator (true if variable length)
     */
    private boolean variableLength;

    /**
     * If variable length, minimum expected length 
     */
    private int minLength;

    /**
     * Authorized data types
     */
    private List<DataType> dataType;

    /**
     * If data type is regexp, gives the expected regexp pattern the data must match
     */
    private String pattern;
    
    /**
     * If data type is date, gives the date format pattern the data must match
     */
    private String datePattern;
    
    public ValidationRule() {
        super();
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the variableLength
     */
    public boolean isVariableLength() {
        return variableLength;
    }

    /**
     * @param variableLength the variableLength to set
     */
    public void setVariableLength(boolean variableLength) {
        this.variableLength = variableLength;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the dataType
     */
    public List<DataType> getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(List<DataType> dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the datePattern
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * @param datePattern the datePattern to set
     */
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    /**
     * @return the minLength
     */
    public int getMinLength() {
        return minLength;
    }

    /**
     * @param minLength the minLength to set
     */
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }
    
}
