package com.googlecode.charpa.service.dao;

import com.googlecode.charpa.service.domain.Application;
import com.googlecode.charpa.service.dao.impl.DaoException;

/**
 * Application dao
 */
public interface IApplicationDao {

    /**
     * Finds application by id
     *
     * @param aApplicationId application id
     * @return application
     * @throws com.googlecode.charpa.service.dao.impl.DaoException
     *          on db error
     */
    Application getApplicationById(long aApplicationId) throws DaoException;

    /**
     * Creates application
     *
     * @param aApplication application to create
     * @throws com.googlecode.charpa.service.dao.impl.DaoException
     *          on db error
     */
    void createApplication(Application aApplication) throws DaoException;
}
