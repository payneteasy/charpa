package com.googlecode.charpa.service.dao.impl.simple;

import com.googlecode.charpa.service.dao.IHostDao;
import com.googlecode.charpa.service.dao.impl.DaoException;
import com.googlecode.charpa.service.domain.Host;

/**
 * Host dao simple implementation
 */
public class HostSimpleDao implements IHostDao {

    public void addHost(Host aHost) throws DaoException {
        aHost.setId(thePersister.getHolder().getNextHostId());
        thePersister.getHolder().getHosts().put(aHost.getId(), aHost);
        thePersister.save();
    }

    public Host getHostById(long aId) {
        return thePersister.getHolder().getHosts().get(aId);
    }

    /** Simple persister */
    public void setPersister(SimplePersister aPersister) { thePersister = aPersister ; }

    /** Simple persister */
    private SimplePersister thePersister ;
}
