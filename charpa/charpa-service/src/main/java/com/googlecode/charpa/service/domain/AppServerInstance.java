package com.googlecode.charpa.service.domain;

import java.io.Serializable;

/**
 * j2ee application server instance
 */
public class AppServerInstance implements Serializable {

    public enum Type {TOMCAT, WEBLOGIC}

    /** Name */
    public String getName() { return theName ; }
    public void setName(String aName) { theName = aName ; }

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

    /** https port */
    private Integer  theHttpsPort ;
    /** http port */
    private int theHttpPort ;
    /** Home directory */
    private String theHomeDirectory ;
    /** Name */
    private String theName ;
    /** Application server type */
    private Type theType ;
}
