package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.LogMessage;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.progress.service.ProgressState;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Progress info
 */
public class ProgressInfo implements Serializable {

    /**
     * Progress info stored in holder
     * @param aId    id
     * @param aName  name
     * @param aMap   page parameters for redirecting
     */
    public ProgressInfo(ProgressId aId, String aName, String aText, String aSecurityInfo, Map<String, String> aMap) {
        theCurrentValue = new AtomicInteger(0);
        theProgressText = new AtomicReference<String>(aText);
        theMax = new AtomicInteger(0);
        theName = new AtomicReference<String>(aName);
        theState = new AtomicReference<ProgressState>(ProgressState.PENDING);
        theId = aId;
        theCreatedTime = new Date();
        theSecurityInfo = aSecurityInfo;
        theMap = Collections.unmodifiableMap(aMap);
    }

    public ProgressId getId() {
        return theId;
    }

    /**
     * Progress text
     */
    public String getProgressText() {
        return theProgressText.get();
    }

    public void setProgressText(String aProgressText) {
        theProgressText.set(aProgressText);
    }

    /**
     * Current Value
     */
    public int getCurrentValue() {
        return theCurrentValue.intValue();
    }

    /**
     * Max
     */
    public int getMax() {
        return theMax.intValue();
    }

    public void setMax(int aMax) {
        theMax.set(aMax);
    }

    public void incrementValue(int delta) {
        theCurrentValue.addAndGet(delta);
        if (theStartedTime.get() != null && getCurrentValue()>0) {
            long one = (System.currentTimeMillis() - theStartedTime.get().getTime()) / getCurrentValue();
            theLeftTime.set(one * (getMax() - getCurrentValue()));
        }
    }

    /**
     * Name
     */
    public String getName() {
        return theName.get();
    }

    public void setName(String aName) {
        theName.set(aName);
    }

    public Date getCreatedTime() {
        return theCreatedTime;
    }

    /**
     * Progress state
     */
    public ProgressState getState() {
        return theState.get();
    }

    public void setState(ProgressState aState) {
        theState.set(aState);
    }

    public Map<String, String> getPageParameters() {
        return theMap;
    }

    public String getSecurityInfo() {
		return theSecurityInfo;
	}

	public long getLeftTime() {
        return theLeftTime.get();
    }
    
    /** Started time */
    public Date getStartedTime() { return theStartedTime.get(); }
    public void setStartedTime(Date aStartedTime) { theStartedTime.set(aStartedTime) ; }

    /** Ended time */
    public Date getEndedTime() { return theEndedTime.get() ; }
    public void setEndedTime(Date aEndedTime) { theEndedTime.set(aEndedTime) ; }

    /**
     * Add info message
     *
     * @param aInfoMessage info message
     */
    public void info(String aInfoMessage) {
        theLogMessages.add(new LogMessage(LogMessage.Level.INFO, aInfoMessage));
    }

    /**
     * Add error message
     *
     * @param aErrorMessage error message
     */
    public void error(String aErrorMessage) {
        theLogMessages.add(new LogMessage(LogMessage.Level.ERROR, aErrorMessage));
    }

    List<LogMessage> getLogMessages() {
        return theLogMessages;
    }
    
    // the following two methods kinda break encapsulation :( they're to be
    // used when restoring a ProgressInfo state from a DB or something like
    // this; they should not be used when working with progresses!
    
    public void setCreatedTime(Date aTime) {
    	theCreatedTime = aTime;
    }
    
    public void setCurrentValue(int aValue) {
    	theCurrentValue.set(aValue);
    }
    
    public void setLeftTime(long aTime) {
    	theLeftTime.set(aTime);
    }
    
    /**
     * Progress state
     */
    private AtomicReference<ProgressState> theState;
    /**
     * Name
     */
    private final AtomicReference<String> theName;
    /**
     * Max
     */
    private final AtomicInteger theMax;
    /**
     * Current Value
     */
    private final AtomicInteger theCurrentValue;
    /**
     * Progress text
     */
    private final AtomicReference<String> theProgressText;

    private final ProgressId theId;
    private Date theCreatedTime;
    private final String theSecurityInfo;
    private final Map<String, String> theMap;
    /** Started time */
    private final AtomicReference<Date> theStartedTime = new AtomicReference<Date>();
    /** Ended time */
    private final AtomicReference<Date> theEndedTime = new AtomicReference<Date>();
    private final AtomicLong theLeftTime = new AtomicLong(0);
    private final List<LogMessage> theLogMessages = Collections.synchronizedList(new LimitedCapacityDroppingList<LogMessage>(50));
}
