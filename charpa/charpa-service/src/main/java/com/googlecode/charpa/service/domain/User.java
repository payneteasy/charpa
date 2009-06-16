package com.googlecode.charpa.service.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * System user
 */
@Root(name = "user")
public class User implements Serializable {
    /** id */
    public long getId() { return theId ; }
    public void setId(long aId) { theId = aId ; }

    /** Username */
    public String getUsername() { return theUsername ; }
    public void setUsername(String aUsername) { theUsername = aUsername ; }

    /** Password */
    public String getPassword() { return thePassword ; }
    public void setPassword(String aPassword) { thePassword = aPassword ; }

    /** Comment */
    public String getComment() { return theComment ; }
    public void setComment(String aComment) { theComment = aComment ; }

    /** Comment */
    @Attribute(name = "comment")
    private String theComment ;
    /** Password */
    @Attribute(name = "password")
    private String thePassword ;
    /** Username */
    @Attribute(name = "username")
    private String theUsername ;
    /** id */
    @Attribute(name = "id")
    private long theId ;
}
