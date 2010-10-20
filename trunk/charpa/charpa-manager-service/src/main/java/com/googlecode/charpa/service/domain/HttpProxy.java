package com.googlecode.charpa.service.domain;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

import java.io.Serializable;

/**
 * Http proxy
 */
@Root(name = "host")
public class HttpProxy implements Serializable {

    /** Hostname */
    public String getHostname() { return theHostname ; }
    public void setHostname(String aHostname) { theHostname = aHostname ; }

    /** Port */
    public int getPort() { return thePort ; }
    public void setPort(int aPort) { thePort = aPort ; }

    /** Port */
    @Attribute(name = "port")
    private int thePort ;
    /** Hostname */
    @Attribute(name = "hostname")
    private String theHostname ;
}
