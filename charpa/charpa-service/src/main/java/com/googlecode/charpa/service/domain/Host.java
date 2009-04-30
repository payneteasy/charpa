package com.googlecode.charpa.service.domain;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

import java.io.Serializable;

/**
 * Host for management
 */
@Root(name = "host")
public class Host implements Serializable {

    /** Id */
    public String getId() { return theId ; }
    public void setId(String aId) { theId = aId ; }

    /** Ip address */
    public String getIpAddress() { return theIpAddress ; }
    public void setIpAddress(String aIpAddress) { theIpAddress = aIpAddress ; }

    /** Ip address */
    @Attribute(name = "address")
    private String theIpAddress ;
    /** Id */
    @Attribute(name = "id")
    private String theId ;
}
