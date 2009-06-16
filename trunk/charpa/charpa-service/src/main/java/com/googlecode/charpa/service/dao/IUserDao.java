package com.googlecode.charpa.service.dao;

import com.googlecode.charpa.service.domain.User;
import com.googlecode.charpa.service.dao.impl.DaoException;

/**
 * User dao
 */
public interface IUserDao {

    /**
     * Finds User by id
     *
     * @param aUserId User id
     * @return User
     * @throws com.googlecode.charpa.service.dao.impl.DaoException
     *          on db error
     */
    User getUserById(long aUserId) throws DaoException;

    /**
     * Creates User
     *
     * @param aUser User to create
     * @throws com.googlecode.charpa.service.dao.impl.DaoException
     *          on db error
     */
    void createUser(User aUser) throws DaoException;
}