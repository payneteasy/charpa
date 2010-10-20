package com.googlecode.charpa.service.model;

import java.io.Serializable;

/**
 * Command for list
 */
public class CommandForList implements Serializable {

    /** Command name */
    public String getCommandName() { return theCommandName ; }
    public void setCommandName(String aCommandName) { theCommandName = aCommandName ; }

    /** Application name */
    public String getApplicationName() { return theApplicationName ; }
    public void setApplicationName(String aApplicationName) { theApplicationName = aApplicationName ; }

    /** Application id */
    public long getApplicationId() { return theApplicationId ; }
    public void setApplicationId(long aApplicationId) { theApplicationId = aApplicationId ; }

    /** Hostname */
    public String getHostname() { return theHostname ; }
    public void setHostname(String aHostname) { theHostname = aHostname ; }

    /** Hostname */
    private String theHostname ;
    /** Application id */
    private long theApplicationId ;
    /** Application name */
    private String theApplicationName ;
    /** Command name */
    private String theCommandName ;
}
