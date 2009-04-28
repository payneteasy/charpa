package com.googlecode.charpa.service.domain;

import java.io.Serializable;

/**
 * Host for management
 */
public class Host implements Serializable {
    
    /** Name */
    public String getName() { return theName ; }
    public void setName(String aName) { theName = aName ; }

    /** Ip address */
    public String getIpAddress() { return theIpAddress ; }
    public void setIpAddress(String aIpAddress) { theIpAddress = aIpAddress ; }

    /** Ip address */
    private String theIpAddress ;
    /** Name */
    private String theName ;
}
