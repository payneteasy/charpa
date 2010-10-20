package com.googlecode.charpa.service.dao.impl.simple;

import com.googlecode.charpa.service.dao.IUserDao;
import com.googlecode.charpa.service.dao.impl.DaoException;
import com.googlecode.charpa.service.domain.User;

/**
 * User's dao simple implementation
 */
public class UserSimpleDao implements IUserDao {

    public void createUser(User aUser) throws DaoException {
        aUser.setId(thePersister.getHolder().getNextUserId());
        thePersister.getHolder().getUsers().put(aUser.getId(), aUser);
        thePersister.save();
    }

    public User getUserById(long aId) {
        return thePersister.getHolder().getUsers().get(aId);
    }

    /** Simple persister */
    public void setPersister(SimplePersister aPersister) { thePersister = aPersister ; }

    /** Simple persister */
    private SimplePersister thePersister ;
}