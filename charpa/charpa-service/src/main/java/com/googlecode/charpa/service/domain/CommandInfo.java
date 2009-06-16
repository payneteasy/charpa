package com.googlecode.charpa.service.domain;

import java.io.Serializable;
import java.io.File;

/**
 * Command info
 */
public class CommandInfo implements Serializable {

    /** Local File */
    public File getLocalFile() { return theLocalFile ; }
    public void setLocalFile(File aLocalFile) { theLocalFile = aLocalFile ; }

    /** Local File */
    private File theLocalFile ;
}
