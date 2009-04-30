package com.googlecode.charpa.service.domain;

import java.io.Serializable;

/**
 * Application
 */
public class Application implements Serializable {
    /** File type */
    public enum FILE_TYPE {WAR, EAR}

    /** Id */
    public String getId() { return theId ; }
    public void setId(String aId) { theId = aId ; }

    /** Filename */
    public String getFilename() { return theFilename ; }
    public void setFilename(String aFilename) { theFilename = aFilename ; }

    /** Filename */
    private String theFilename ;
    /** Id */
    private String theId ;
}
