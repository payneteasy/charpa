package com.googlecode.charpa.service.domain;

import java.io.Serializable;

/**
 * Application
 */
public class Application implements Serializable {

    /** Application id */
    public ApplicationId getApplicationId() { return theApplicationId ; }
    public void setApplicationId(ApplicationId aApplicationId) { theApplicationId = aApplicationId ; }

    /** Application name */
    public String getApplicationName() { return theApplicationName ; }
    public void setApplicationName(String aApplicationName) { theApplicationName = aApplicationName ; }

    /** Application name */
    private String theApplicationName ;
    /** Application id */
    private ApplicationId theApplicationId ;
}
