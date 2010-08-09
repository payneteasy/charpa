package com.googlecode.charpa.progress.service;

import java.io.Serializable;

/**
 * Progress id
 */
public class ProgressId implements Serializable {

    public ProgressId(String aProgressIdAsString) {
        theProgressIdAsString = aProgressIdAsString;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgressId that = (ProgressId) o;

        return theProgressIdAsString.equals(that.theProgressIdAsString);

    }

    public int hashCode() {
        return theProgressIdAsString.hashCode();
    }

    @Override
    public String toString() {
        return theProgressIdAsString;
    }

    private String theProgressIdAsString ;
}
