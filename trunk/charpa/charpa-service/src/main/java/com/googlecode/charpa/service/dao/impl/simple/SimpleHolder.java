package com.googlecode.charpa.service.dao.impl.simple;

import com.googlecode.charpa.service.domain.Host;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Holder for simple
 */
@Root(name = "config")
public class SimpleHolder implements Serializable {

    /** Hosts */
    public Map<String, Host> getHosts() { return theHosts ; }

    /** Hosts */
    @ElementMap(entry="hosts", key="id", attribute=true, inline=true
            , valueType =Host.class, keyType = String.class)
    private Map<String, Host> theHosts = new HashMap<String, Host>();
}
