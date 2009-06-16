package com.googlecode.charpa.service.domain;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

import java.io.Serializable;

/**
 * Host for management
 */
@Root(name = "host")
public class Host implements Serializable {

    /** Host id */
    public long getId() { return theId ; }
    public void setId(long aId) { theId = aId ; }

    /** Name */
    public String getName() { return theName ; }
    public void setName(String aName) { theName = aName ; }

    /** Hostname or ip address */
    public String getHostname() { return theHostname ; }
    public void setHostname(String aHostname) { theHostname = aHostname ; }

    /** SSH port */
    public int getSshPort() { return theSshPort ; }
    public void setSshPort(int aSshPort) { theSshPort = aSshPort ; }

    /** SSH port */
    @Attribute(name = "sshPort")
    private int theSshPort = 22;
    /** Name */
    @Attribute(name = "name")
    private String theName ;
    /** Hostname */
    @Attribute(name = "hostname")
    private String theHostname ;
    /** Host id */
    @Attribute(name = "id")
    private long theId ;
}
