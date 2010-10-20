package com.googlecode.charpa.service.domain;

import com.googlecode.charpa.service.model.VariableInfo;

import java.io.Serializable;
import java.io.File;
import java.util.List;
import java.util.LinkedList;

/**
 * Command info
 */
public class CommandInfo implements Serializable {

    /** Local File */
    public File getLocalFile() { return theLocalFile ; }
    public void setLocalFile(File aLocalFile) { theLocalFile = aLocalFile ; }

    /** Variables */
    public List<VariableInfo> getVariables() { return theVariables ; }

    /** Application name */
    public String getApplicationName() { return theApplicationName ; }
    public void setApplicationName(String aApplicationName) { theApplicationName = aApplicationName ; }

    /** Hostname */
    public String getHostname() { return theHostname ; }
    public void setHostname(String aHostname) { theHostname = aHostname ; }

    /** Hostname */
    private String theHostname ;
    /** Application name */
    private String theApplicationName ;
    /** Variables */
    private List<VariableInfo> theVariables = new LinkedList<VariableInfo>();
    /** Local File */
    private File theLocalFile ;
}
