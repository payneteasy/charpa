package com.googlecode.charpa.service.impl;

import junit.framework.TestCase;
import com.googlecode.charpa.service.SecurityShellException;
import com.googlecode.charpa.service.ICommandOutputListener;

/**
 * 
 */
public class SecurityShellServiceImplTest extends TestCase {

    public void test() throws SecurityShellException, InterruptedException {
        SecurityShellServiceImpl service = new SecurityShellServiceImpl();
        ICommandOutputListener listener = new ICommandOutputListener() {
            public void onOutputLine(Level aLevel, String aLine) {
                if(aLevel== Level.OUTPUT) {
                    System.out.println(aLine);
                } else {
                    System.err.println(aLine);
                }
            }
        };
        service.executeCommand("localhost", 22, "test", "test", null, "unknowncommand", null, listener);
    }
}
