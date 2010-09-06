package com.googlecode.charpa.progress.service;

/**
 * Progress state
 */
public enum ProgressState {

    /** Task is put to queue */
    PENDING

    /** Task is running */
    , RUNNING

    /** Task is cancelled */
    , CANCELLED

    /** Task is failed*/
    , FAILED

    /** Task is finished successfully */
    , FINISHED
    
}
