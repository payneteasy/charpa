package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.IProgressInfo;
import com.googlecode.charpa.progress.service.ProgressState;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.progress.service.LogMessage;

import java.util.*;

import org.joda.time.Period;

/**
 *  Immutable ProgressInfo
 */
public class ImmutableProgressInfo implements IProgressInfo {
    public ImmutableProgressInfo(ProgressId aId, String aName, int aMax, String aText, int aValue
            , ProgressState aState, Map<String, String> aPageParameters
            , Date aCreatedTime, Date aStartedTime, Date aEndedTime
            , Period aElapsedTime, Period aLeftTime
            , List<LogMessage> aLogMessages  
    ) {
        theMax = aMax;
        theText = aText;
        theValue = aValue;
        theName = aName;
        theState = aState;
        theProgressId = aId;
        thePageParameters = aPageParameters;
        theCreatedTime = aCreatedTime;
        theEndedTime = aEndedTime;
        theLeftTime = aLeftTime;
        theElapsedTime = aElapsedTime;
        theStartedTime = aStartedTime;
        theLogMessages = aLogMessages;
    }

    public String getName() {
        return theName;
    }

    public String getProgressText() {
        return theText;
    }

    public int getCurrentValue() {
        return theValue;
    }

    public int getMax() {
        return theMax ;
    }

    public ProgressState getState() {
        return theState;
    }

    public ProgressId getId() {
        return theProgressId;
    }

    public Map<String, String> getPageParameters() {
        return thePageParameters;
    }

    public Date getStartedTime() {
        return theStartedTime;
    }

    public Date getEndedTime() {
        return theEndedTime;
    }

    public Date getCreatedTime() {
        return theCreatedTime;
    }

    public Period getElapsedPeriod() {
        return theElapsedTime;
    }

    public Period getLeftPeriod() {
        return theLeftTime;
    }

    /**
     * {@inheritDoc}
     */
    public List<LogMessage> getLastLogMessages(int aCount) {
        LinkedList<LogMessage> list = new LinkedList<LogMessage>();
        int size = theLogMessages.size();
        for(int i=0; i<aCount && i < size; i++) {
            list.add(0, theLogMessages.get(size - i - 1));
        }
        return list;
    }

    private final ProgressId theProgressId;
    private final int theMax;
    private final String theText;
    private final int theValue;
    private final String theName;
    private final ProgressState theState;
    private final Map<String, String> thePageParameters;
    private final Date theStartedTime;
    private final Date theEndedTime;
    private final Date theCreatedTime;
    private final Period theElapsedTime;
    private final Period theLeftTime;
    private final List<LogMessage> theLogMessages;
}
