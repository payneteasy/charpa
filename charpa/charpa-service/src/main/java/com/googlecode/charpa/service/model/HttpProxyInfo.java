package com.googlecode.charpa.service.model;

/**
 *  Http proxy host and port
 */
public class HttpProxyInfo {

    /**
     * Create http proxy info
     * @param aHostname  host
     * @param aPort      port
     */
    public HttpProxyInfo(String aHostname, int aPort) {
        theHostname = aHostname ;
        thePort = aPort;
    }

    /** Hostname */
    public String getHostname() { return theHostname ; }
    public void setHostname(String aHostname) { theHostname = aHostname ; }

    /** port */
    public int getPort() { return thePort ; }
    public void setPort(int aPort) { thePort = aPort ; }

    /** port */
    private int thePort ;
    /** Hostname */
    private String theHostname ;
}
