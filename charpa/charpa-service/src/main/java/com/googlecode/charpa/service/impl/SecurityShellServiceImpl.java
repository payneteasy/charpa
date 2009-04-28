package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.service.ISecurityShellService;
import com.googlecode.charpa.service.ICommandOutputListener;
import com.googlecode.charpa.service.SecurityShellException;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ChannelExec;

import java.util.Map;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;

import org.springframework.util.Assert;

/**
 * Default implementation of ssh service
 */
public class SecurityShellServiceImpl implements ISecurityShellService {
    private static final int DEFAULT_TIMEOUT = 30000;

    /**
     * {@inheritDoc}
     */
    public void executeCommand(String aHostname, int aPort
            , String aUsername, String aPassword
            , Map<String, String> aEnv, String aCommand, List<String> aArguments
            , ICommandOutputListener aCommandOutputListener)
            throws SecurityShellException {
        Assert.notNull(aCommandOutputListener, "aCommandOutputListener is null");
        
        JSch jsch = new JSch();

        try {
            Session session = jsch.getSession(aUsername, aHostname, aPort);
            session.setPassword(aPassword);
            session.setUserInfo(new UserInfoKI(aPassword));

            try {
                session.connect(DEFAULT_TIMEOUT);

                try {
                    ChannelExec channel = (ChannelExec) session.openChannel("exec");
                    try {
                        channel.setCommand(aCommand);
                        channel.setInputStream(null);
                        channel.setErrStream(null);
                        channel.setOutputStream(System.out);

                        channel.connect(DEFAULT_TIMEOUT);
                        try {
                            try {
                                processExec(channel.getInputStream(), channel.getErrStream(), channel, aCommandOutputListener);
                            } catch (IOException e) {
                                throw new SecurityShellException("Error processing command:" + e.getMessage(), e);
                            }
                        } finally {
                            channel.disconnect();
                        }
                    } catch (Exception e) {
                        throw new SecurityShellException("Error connecting to channel channel:" + e.getMessage(), e);
                    }
                } catch (Exception e) {
                    throw new SecurityShellException("Error opening channel: " + e.getMessage(), e);
                } finally {
                    session.disconnect();
                }
            } catch (JSchException e) {
                throw new SecurityShellException("Error connecting to host: " + aHostname + ":" + aPort
                        + " : " + e.getMessage()
                        , e);
            }
        } catch (JSchException e) {
            throw new SecurityShellException("Error connecting to host: " + aHostname + ":" + aPort
                    + " : " + e.getMessage()
                    , e);
        }
    }

    private void processExec(InputStream aInputStream, InputStream aErrStream
            , ChannelExec aExcelChannel, ICommandOutputListener aCommandOutputListener) throws IOException {
        while (true) {
            processStream(aInputStream, aCommandOutputListener, ICommandOutputListener.Level.OUTPUT);
            processStream(aErrStream, aCommandOutputListener, ICommandOutputListener.Level.ERROR);

            if (aExcelChannel.isClosed()) {
                System.out.println("exit-status: " + aExcelChannel.getExitStatus());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    private void processStream(InputStream aInputStream, ICommandOutputListener aCommandOutputListener, ICommandOutputListener.Level aLevel) throws IOException {
        byte[] buf = new byte[1024];
        while (aInputStream.available() > 0) {
            int i = aInputStream.read(buf, 0, 1024);
            if (i < 0) break;
            aCommandOutputListener.onOutputLine(aLevel, new String(buf, 0, i));
        }
    }
}
