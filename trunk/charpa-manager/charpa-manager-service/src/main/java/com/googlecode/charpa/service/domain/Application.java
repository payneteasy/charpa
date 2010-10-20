package com.googlecode.charpa.service.domain;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

import java.io.Serializable;

/**
 * Application
 */
@Root(name = "application")
public class Application implements Serializable {

    /** id */
    public long getId() { return theId ; }
    public void setId(long aId) { theId = aId ; }

    /** Host id */
    public Long getHostId() { return theHostId ; }
    public void setHostId(Long  aHostId) { theHostId = aHostId ; }

    /** User id */
    public Long getUserId() { return theUserId ; }
    public void setUserId(Long aUserId) { theUserId = aUserId ; }

    /** Application name */
    public String getApplicationName() { return theApplicationName ; }
    public void setApplicationName(String aApplicationName) { theApplicationName = aApplicationName ; }

    /** Application name */
    @Attribute(name = "name")
    private String theApplicationName ;
    /** id */
    @Attribute(name = "id")
    private long theId ;
    /** Host id */
    @Attribute(name = "hostId")
    private Long  theHostId ;
    /** User id */
    @Attribute(name = "userId")
    private Long theUserId ;
}
