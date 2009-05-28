package com.googlecode.charpa.service.domain;

import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Application id
 */
public class ApplicationId implements Serializable {

    public ApplicationId(String aHostname, String aApplicationName) {
        Assert.notNull(aHostname, "hostname must not be null");
        Assert.notNull(aApplicationName, "must not be null");
        theHostname = aHostname;
        theApplicationName = aApplicationName;
    }

    /**
     * Hostname
     *
     * @return hostname
     */
    public String getHostname() {
        return theHostname;
    }

    /**
     * Application name
     *
     * @return application name
     */
    public String getApplicationName() {
        return theApplicationName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationId that = (ApplicationId) o;

        if (!theApplicationName.equals(that.theApplicationName)) return false;
        if (!theHostname.equals(that.theHostname)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = theApplicationName.hashCode();
        result = 31 * result + theHostname.hashCode();
        return result;
    }

    public String toString() {
        return "AppId[" + theHostname + "/" + theApplicationName + "]";
    }

    /**
     * Application name
     */
    private final String theApplicationName;
    /**
     * Hostname
     */
    private final String theHostname;
}
