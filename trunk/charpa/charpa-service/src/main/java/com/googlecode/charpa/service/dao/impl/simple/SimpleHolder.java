package com.googlecode.charpa.service.dao.impl.simple;

import com.googlecode.charpa.service.domain.Host;
import com.googlecode.charpa.service.domain.Application;
import com.googlecode.charpa.service.domain.User;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Holder for simple
 */
@Root(name = "config")
public class SimpleHolder implements Serializable {

    /**
     * Hosts
     */
    public Map<Long, Host> getHosts() {
        return theHosts;
    }

    /**
     * Users
     */
    public Map<Long, User> getUsers() {
        return theUsers;
    }

    public Map<Long, Application> getApplications() {
        return theApplications;
    }
    
    /**
     * Next Host id
     */
    public long getNextHostId() {
        return theNextHostId++;
    }

    public void setNextHostId(long aNextHostId) {
        theNextHostId = aNextHostId;
    }

    /**
     * Next application id
     */
    public long getNextApplicationId() {
        return theNextApplicationId++;
    }

    public void setNextApplicationId(long aNextApplicationId) {
        theNextApplicationId = aNextApplicationId;
    }

    /** Next user id */
    public long getNextUserId() { return theNextUserId++ ; }
    public void setNextUserId(long aNextUserId) { theNextUserId = aNextUserId ; }

    /** Next user id */
    @Attribute(name = "nextUserId")
    private long theNextUserId = 1;
    /**
     * Next application id
     */
    @Attribute(name = "nextApplicationId")
    private long theNextApplicationId = 1;
    /**
     * Next Host id
     */
    @Attribute(name = "nextHostId")
    private long theNextHostId = 1;

    /**
     * Hosts
     */
    @ElementMap(entry = "hosts", key = "id", attribute = true, inline = true
            , valueType = Host.class, keyType = Long.class)
    private Map<Long, Host> theHosts = new HashMap<Long, Host>();

    /**
     * Applications
     */
    @ElementMap(entry = "applications", key = "id", attribute = true, inline = true
            , valueType = Application.class, keyType = Long.class)
    private Map<Long, Application> theApplications = new HashMap<Long, Application>();

    /**
     * Users
     */
    @ElementMap(entry = "users", key = "id", attribute = true, inline = true
            , valueType = User.class, keyType = Long.class)
    private Map<Long, User> theUsers = new HashMap<Long, User>();
}
