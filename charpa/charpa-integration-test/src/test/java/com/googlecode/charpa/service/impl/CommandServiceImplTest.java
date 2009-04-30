package com.googlecode.charpa.service.impl;

import junit.framework.TestCase;
import com.googlecode.charpa.service.domain.Host;
import com.googlecode.charpa.service.domain.Application;
import com.googlecode.charpa.service.domain.User;
import com.googlecode.charpa.service.domain.AppServerInstance;

/**
 * 
 */
public class CommandServiceImplTest extends TestCase {

    public void test() {
        Host host = new Host();
        host.setId("localhost");
        host.setIpAddress("localhost");

        Application app = new Application();
        app.setId("test-app");
        app.setFilename("test-app.war");

        User user = new User();
        user.setId("test");
        user.setUsername("test");
        user.setPassword("test");

        AppServerInstance instance = new AppServerInstance();
        instance.setApplicationId("test-app");
        instance.setHostId("localhost");
        instance.setUserId("test");
        instance.setHomeDirectory("~/opt/tomcat1");
        instance.setHttpPort(8080);
        instance.setHttpsPort(null);
        instance.setType(AppServerInstance.Type.TOMCAT);

        
    }
}
