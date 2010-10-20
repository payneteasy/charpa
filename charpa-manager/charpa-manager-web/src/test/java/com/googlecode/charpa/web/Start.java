package com.googlecode.charpa.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


public class Start {

    public static void main(String[] args) throws Exception {
        final Server server = new Server(8080);

//        SocketConnector connector = new SocketConnector();
//        // Set some timeout options to make debugging easier.
//        connector.setMaxIdleTime(1000 * 60 * 60);
//        connector.setSoLingerTime(-1);
//        connector.setPort(8086);
//        server.setConnectors(new Connector[]{connector});

        WebAppContext context = new WebAppContext();
        context.setContextPath("/charpa");
        context.setWar("src/main/webapp");
        server.setHandler(context);

        server.start();
        server.join();
    }
}
