package com.googlecode.charpa.service;

import com.googlecode.charpa.service.domain.Host;

/**
 * Host management service
 */
public interface IHostService {

    /**
     * Adds host
     * @param aHost host
     */
    void addHost(Host aHost);

    /**
     * Finds host by id
     * @param aId host id
     * @return host
     */
    Host getHostById(long aId);
}
