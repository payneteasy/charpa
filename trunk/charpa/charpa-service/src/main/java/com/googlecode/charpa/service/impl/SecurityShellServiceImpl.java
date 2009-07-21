package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.service.ISecurityShellService;
import com.googlecode.charpa.service.ICommandOutputListener;
import com.googlecode.charpa.service.SecurityShellException;
import com.googlecode.charpa.service.model.HttpProxyInfo;
import com.jcraft.jsch.*;

import java.util.Map;
import java.util.List;
import java.util.StringTokenizer;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;

import org.springframework.util.Assert;

/**
 * Default implementation of ssh service
 */
public class SecurityShellServiceImpl implements ISecurityShellService {
    private static final int DEFAULT_TIMEOUT = 30000;

    /**
     * {@inheritDoc}
     */
    public void copyFileToRemoteHost(String aHostname
            , int aPort
            , String aUsername
            , String aPassword
            , File aLocalFile
            , String aRemoteDir
            , HttpProxyInfo aProxyInfo )
            throws SecurityShellException {

        Session session = createSession(aHostname, aPort, aUsername, aPassword, aProxyInfo);
        try {
            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
            try {
                try {
                    sftp.connect();
                    try {
                        sftp.cd(aRemoteDir);
                        String localDir = aLocalFile.getParentFile().getAbsolutePath();
                        try {
                            sftp.lcd(localDir);
                            String filename = aLocalFile.getName();
                            try {
                                sftp.put(filename, filename, ChannelSftp.OVERWRITE);
                            } catch (SftpException e) {
                                throw new SecurityShellException("Error copying file '" + filename + "' :" + e.getMessage(), e);
                            }
                        } catch (SftpException e) {
                            throw new SecurityShellException("Error changing local dir to '" + aLocalFile.getAbsolutePath() + "' :" + e.getMessage(), e);
                        }
                    } catch (SftpException e) {
                        throw new SecurityShellException("Error changining remote dir to '" + aRemoteDir + "' :" + e.getMessage(), e);
                    }
                } catch (JSchException e) {
                    throw new SecurityShellException("Error opening sftp channel: " + e.getMessage(), e);
                }
            } finally {
                sftp.disconnect();
            }
        } catch (JSchException e) {
            throw new SecurityShellException("Error opening sftp channel: " + e.getMessage(), e);
        } finally {
            closeSession(session);
        }
    }

    private void closeSession(Session aSession) {
        aSession.disconnect();
    }

    private Session createSession(String aHostname, int aPort, String aUsername, String aPassword, HttpProxyInfo aProxyInfo) throws SecurityShellException {
        JSch jsch = new JSch();

        try {
            Session session = jsch.getSession(aUsername, aHostname, aPort);
            session.setPassword(aPassword);
            session.setUserInfo(new UserInfoKI(aPassword));
            if (aProxyInfo != null) {
                session.setProxy(new ProxyHTTP(aProxyInfo.getHostname(), aProxyInfo.getPort()));
            }

            try {
                session.connect(DEFAULT_TIMEOUT);

                return session;
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

    /**
     * {@inheritDoc}
     */
    public void executeCommand(String aHostname, int aPort
            , String aUsername, String aPassword
            , Map<String, String> aEnv, String aCommand, List<String> aArguments
            , ICommandOutputListener aCommandOutputListener
            , HttpProxyInfo aProxyInfo )
            throws SecurityShellException {
        Assert.notNull(aCommandOutputListener, "aCommandOutputListener is null");

        Session session = createSession(aHostname, aPort, aUsername, aPassword, aProxyInfo);

        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            try {

                channel.setInputStream(null);
                channel.setErrStream(null);
                channel.setOutputStream(System.out);

                // env variables
                for (Map.Entry<String, String> entry : aEnv.entrySet()) {
                    channel.setEnv(entry.getKey().getBytes(), entry.getValue().getBytes());
                }
                
                channel.setCommand(aCommand);

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
            closeSession(session);
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
            String text = new String(buf, 0, i);
            StringTokenizer st = new StringTokenizer(text, "\r\n");
            while( st.hasMoreTokens() ) {
                aCommandOutputListener.onOutputLine(aLevel, st.nextToken());
            }
        }
    }
}
