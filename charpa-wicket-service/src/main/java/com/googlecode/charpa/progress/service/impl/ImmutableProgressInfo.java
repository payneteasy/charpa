package com.googlecode.charpa.progress.service.impl;

import java.util.Date;
import java.util.Map;

import org.joda.time.Period;

import com.googlecode.charpa.progress.service.IProgressInfo;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.progress.service.ProgressState;

/**
 *  Immutable ProgressInfo
 */
public class ImmutableProgressInfo implements IProgressInfo {
    public ImmutableProgressInfo(ProgressId aId, String aName, int aMax, String aText, int aValue
            , ProgressState aState, Map<String, String> aPageParameters
            , Date aCreatedTime, Date aStartedTime, Date aEndedTime
            , Period aElapsedTime, Period aLeftTime
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
}
