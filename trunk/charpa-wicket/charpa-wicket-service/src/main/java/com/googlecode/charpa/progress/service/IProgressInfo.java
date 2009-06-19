package com.googlecode.charpa.progress.service;

import org.joda.time.Period;

import java.util.Map;
import java.util.Date;
import java.util.List;

/**
 * Progress info
 */
public interface IProgressInfo {

    /**
     * Progress Id
     * @return id
     */
    ProgressId getId();

    /**
     * Progress name
     * @return name
     */
    String getName();
    
    /**
     * Text in progress box
     * @return text
     */
    String getProgressText();

    /**
     * Current progress value
     * @return progress value
     */
    int getCurrentValue();

    /**
     * Maximum progress value
     * @return maximum value
     */
    int getMax();

    /**
     * Progress state
     * @return state
     */
    ProgressState getState();

    /**
     * Stored Page parameters
     * @return page parameters
     */
    Map<String, String> getPageParameters();

    /**
     * Started time, when progress was started
     * @return started time
     */
    Date getStartedTime() ;

    /**
     * Ended time, i.e. finished, failded or cancelled
     * @return ended time
     */
    Date getEndedTime() ;

    /**
     * Created time, when progress was added
     * @return created time
     */
    Date getCreatedTime();

    /**
     * Elapsed time
     * @return Elapsed time
     */
    Period getElapsedPeriod();

    /**
     * Left time
     * @return left time
     */
    Period getLeftPeriod();

    /**
     * Last log messages
     * @param aCount count messages
     * @return log messages
     */
    List<LogMessage> getLastLogMessages(int aCount);

}
