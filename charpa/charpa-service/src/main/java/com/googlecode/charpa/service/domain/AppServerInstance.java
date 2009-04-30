package com.googlecode.charpa.service.domain;

import java.io.Serializable;

/**
 * j2ee application server instance
 */
public class AppServerInstance implements Serializable {

    public enum Type {TOMCAT, WEBLOGIC}

    /** Home directory */
    public String getHomeDirectory() { return theHomeDirectory ; }
    public void setHomeDirectory(String aHomeDirectory) { theHomeDirectory = aHomeDirectory ; }

    /** http port */
    public int getHttpPort() { return theHttpPort ; }
    public void setHttpPort(int aHttpPort) { theHttpPort = aHttpPort ; }

    /** Application server type */
    public Type getType() { return theType ; }
    public void setType(Type aType) { theType = aType ; }

    /**
     * https port
     * @return null if no https protocol 
     */
    public Integer  getHttpsPort() { return theHttpsPort ; }
    public void setHttpsPort(Integer  aHttpsPort) { theHttpsPort = aHttpsPort ; }

    /** User id */
    public String getUserId() { return theUserId ; }
    public void setUserId(String aUserId) { theUserId = aUserId ; }

    /** Application id */
    public String getApplicationId() { return theApplicationId ; }
    public void setApplicationId(String aApplicationId) { theApplicationId = aApplicationId ; }

    /** Host */
    public String getHostId() { return theHostId ; }
    public void setHostId(String aHostId) { theHostId = aHostId ; }

    /** Host */
    private String theHostId ;
    /** Application id */
    private String theApplicationId ;
    /** User id */
    private String theUserId ;
    /** https port */
    private Integer  theHttpsPort ;
    /** http port */
    private int theHttpPort ;
    /** Home directory */
    private String theHomeDirectory ;
    /** Application server type */
    private Type theType ;
}
