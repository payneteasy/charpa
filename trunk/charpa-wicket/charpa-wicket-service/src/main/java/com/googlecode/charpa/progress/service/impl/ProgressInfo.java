package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.IProgressInfo;
import com.googlecode.charpa.progress.service.ProgressState;
import com.googlecode.charpa.progress.service.ProgressId;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.Map;
import java.util.Collections;

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
    public ProgressInfo(ProgressId aId, String aName, Map<String, String> aMap) {
        theCurrentValue = new AtomicInteger(0);
        theProgressText = new AtomicReference<String>("Starting...");
        theMax = new AtomicInteger(0);
        theName = new AtomicReference<String>(aName);
        theState = new AtomicReference<ProgressState>(ProgressState.PENDING);
        theId = aId;
        theCreatedTime = new Date();
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

    public void incrementValue() {
        theCurrentValue.incrementAndGet();
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
    private final Date theCreatedTime;
    private final Map<String, String> theMap;
    /** Started time */
    private final AtomicReference<Date> theStartedTime = new AtomicReference<Date>();
    /** Ended time */
    private final AtomicReference<Date> theEndedTime = new AtomicReference<Date>();
    private final AtomicLong theLeftTime = new AtomicLong(0);  
}
