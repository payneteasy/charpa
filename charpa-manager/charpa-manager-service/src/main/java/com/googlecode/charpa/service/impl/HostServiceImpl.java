package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.service.IHostService;
import com.googlecode.charpa.service.domain.Host;
import com.googlecode.charpa.service.dao.IHostDao;
import com.googlecode.charpa.service.dao.impl.DaoException;

/**
 * Host service implementation
 */
public class HostServiceImpl implements IHostService {

    /**
     * {@inheritDoc}
     */
    public void addHost(Host aHost) {
        try {
            theHostDao.addHost(aHost);
        } catch (DaoException e) {
            throw new RuntimeException("Error adding host "+aHost+": "+e.getMessage(), e) ;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Host getHostById(long aId) {
        try {
            return theHostDao.getHostById(aId);
        } catch (DaoException e) {
            throw new RuntimeException("Error getting host "+aId+": "+e.getMessage(), e) ;
        }
    }

    /** IHostDao */
    public void setHostDao(IHostDao aHostDao) { theHostDao = aHostDao ; }

    /** IHostDao */
    private IHostDao theHostDao ;
}
