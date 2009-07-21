package com.googlecode.charpa.service.model;

import java.io.Serializable;

/**
 * Variable info
 */
public class VariableInfo implements Serializable {
    /** Variable name */
    public String getName() { return theName ; }
    public void setName(String aName) { theName = aName ; }

    /** Variable comment */
    public String getComment() { return theComment ; }
    public void setComment(String aComment) { theComment = aComment ; }

    /** default value */
    public String getDefaultValue() { return theDefaultValue ; }
    public void setDefaultValue(String aDefaultValue) { theDefaultValue = aDefaultValue ; }

    /** value */
    public String getValue() { return theValue ; }
    public void setValue(String aValue) { theValue = aValue ; }
    
    /** value */
    private String theValue ;
    /** default value */
    private String theDefaultValue ;
    /** Variable comment */
    private String theComment ;
    /** Variable name */
    private String theName ;
}
