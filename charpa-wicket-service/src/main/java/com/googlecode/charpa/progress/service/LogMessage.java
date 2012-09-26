package com.googlecode.charpa.progress.service;

import java.io.Serializable;

/**
 * Log message
 */
public class LogMessage implements Serializable {
    public enum Level {INFO, ERROR}

    public LogMessage(Level aLevel, String aMessage) {
        theLevel = aLevel;
        theMessage = aMessage;
    }

    /** Level */
    public Level getLevel() { return theLevel ; }

    /** Message */
    public String getMessage() { return theMessage ; }

    /** Message */
    private final String theMessage ;
    /** Level */
    private final Level theLevel ;

    @Override
    public String toString() {
        return theMessage;
    }
}
