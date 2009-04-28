package com.googlecode.charpa.service.domain;

import java.io.Serializable;

/**
 * Application
 */
public class Application implements Serializable {
    /** File type */
    public enum FILE_TYPE {WAR, EAR}

    /** Application name */
    public String getName() { return theName ; }
    public void setName(String aName) { theName = aName ; }

    /** Filename */
    public String getFilename() { return theFilename ; }
    public void setFilename(String aFilename) { theFilename = aFilename ; }

    /** Filename */
    private String theFilename ;
    /** Application name */
    private String theName ;
}
