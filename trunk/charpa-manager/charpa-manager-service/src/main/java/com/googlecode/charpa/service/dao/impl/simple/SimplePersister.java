package com.googlecode.charpa.service.dao.impl.simple;

import com.googlecode.charpa.service.dao.impl.DaoException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

/**
 * Persister for simple framework
 */
public class SimplePersister {

    public SimplePersister(String aFilename) throws Exception {
        Serializer serializer = new Persister();
        
        theFile = new File(aFilename);
        if(theFile.exists()) {
            theHolder = serializer.read(SimpleHolder.class, theFile);
        } else {
            theHolder = new SimpleHolder();
        }
    }

    /** Get holder */
    public SimpleHolder getHolder() { return theHolder ; }

    /**
     * Saves
     * @throws com.googlecode.charpa.service.dao.impl.DaoException on save exception
     */
    public synchronized void save() throws DaoException {
        Serializer serializer = new Persister();
        try {
            serializer.write(theHolder, theFile);
        } catch (Exception e) {
            throw new DaoException("Could not save file "+theFile.getAbsolutePath()+": "+e.getMessage(), e);
        }
    }

    /** Filename */
    private final File theFile ;
    /** Get holder */
    private final SimpleHolder theHolder ;
}
