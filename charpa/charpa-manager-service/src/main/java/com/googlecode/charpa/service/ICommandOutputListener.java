package com.googlecode.charpa.service;

/**
 * Command output listener
 */
public interface ICommandOutputListener {
    public enum Level {OUTPUT, ERROR}
    /**
     * On std output line
     * @param aLevel level
     * @param aLine line
     */
    void onOutputLine(Level aLevel, String aLine);

}
