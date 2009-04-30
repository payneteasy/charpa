package com.googlecode.charpa.service.domain;

import java.io.Serializable;

/**
 * System user
 */
public class User implements Serializable {
    /** id */
    public String getId() { return theId ; }
    public void setId(String aId) { theId = aId ; }

    /** Username */
    public String getUsername() { return theUsername ; }
    public void setUsername(String aUsername) { theUsername = aUsername ; }

    /** Password */
    public String getPassword() { return thePassword ; }
    public void setPassword(String aPassword) { thePassword = aPassword ; }

    /** Password */
    private String thePassword ;
    /** Username */
    private String theUsername ;
    /** id */
    private String theId ;
}
