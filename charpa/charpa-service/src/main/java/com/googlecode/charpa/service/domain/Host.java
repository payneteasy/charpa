package com.googlecode.charpa.service.domain;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

import java.io.Serializable;

/**
 * Host for management
 */
@Root(name = "host")
public class Host implements Serializable {

    /** Hostname */
    public String getHostname() { return theHostname ; }
    public void setHostname(String aHostname) { theHostname = aHostname ; }

    /** Ip address */
    public String getIpAddress() { return theIpAddress ; }
    public void setIpAddress(String aIpAddress) { theIpAddress = aIpAddress ; }

    /** Ip address */
    @Attribute(name = "ipAddress")
    private String theIpAddress ;
    /** Hostname */
    @Attribute(name = "hostname")
    private String theHostname ;
}
