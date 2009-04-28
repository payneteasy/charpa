package com.googlecode.charpa.service.impl;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * Keyboard interactive login to ssh
 */
public class UserInfoKI implements UserInfo, UIKeyboardInteractive {

    public UserInfoKI(String aPassword) {
        thePassword = aPassword;
    }

    public String getPassphrase() {
        return null;
    }

    public String getPassword() {
        return thePassword;
    }

    public boolean promptPassword(String s) {
        return false;
    }

    public boolean promptPassphrase(String s) {
        return false;
    }

    public boolean promptYesNo(String s) {
        return true;
    }

    public void showMessage(String s) {
        // todo show message
    }

    public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
                                              boolean[] echo) {

        String[] ret;

        if (prompt.length == echo.length && prompt.length == 1 && !echo[0]
                && prompt[0].toLowerCase().indexOf("password") > -1) {

            ret = new String[1];
            ret[0] = thePassword;
        } else {
            // jsch-0.1.21/examples/UserAuthKI.java returns null to cancel
            ret = null;
        }

        return ret;
    }

    private final String thePassword;
}
