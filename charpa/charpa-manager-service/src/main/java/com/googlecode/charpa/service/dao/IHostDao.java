package com.googlecode.charpa.service.dao;

import com.googlecode.charpa.service.dao.impl.DaoException;
import com.googlecode.charpa.service.domain.Host;

/**
 * Persists host
 */
/**
 * Host management service
 */
public interface IHostDao {

    /**
     * Adds host
     * @param aHost host
     */
    void addHost(Host aHost) throws DaoException;

    /**
     * Finds host by id
     * @param aId host id
     * @return host
     */
    Host getHostById(long aId) throws DaoException;
}
