package com.googlecode.charpa.service.dao.impl.simple;

import com.googlecode.charpa.service.dao.IApplicationDao;
import com.googlecode.charpa.service.dao.impl.DaoException;
import com.googlecode.charpa.service.domain.Application;

/**
 * Application's dao simple implementation
 */
public class ApplicationSimpleDao implements IApplicationDao {

    public void createApplication(Application aApplication) throws DaoException {
        aApplication.setId(thePersister.getHolder().getNextApplicationId());
        thePersister.getHolder().getApplications().put(aApplication.getId(), aApplication);
        thePersister.save();
    }

    public Application getApplicationById(long aId) {
        return thePersister.getHolder().getApplications().get(aId);
    }

    /** Simple persister */
    public void setPersister(SimplePersister aPersister) { thePersister = aPersister ; }

    /** Simple persister */
    private SimplePersister thePersister ;
}
