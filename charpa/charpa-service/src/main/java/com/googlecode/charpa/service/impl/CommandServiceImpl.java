package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.progress.service.IProgressManagerService;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.service.*;
import com.googlecode.charpa.service.dao.IApplicationDao;
import com.googlecode.charpa.service.dao.IUserDao;
import com.googlecode.charpa.service.domain.*;
import com.googlecode.charpa.service.model.HttpProxyInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;

import org.springframework.util.Assert;
import org.apache.wicket.util.io.IOUtils;

/**
 * Implementation of ICommandService
 */
public class CommandServiceImpl implements ICommandService {

    /**
     * {@inheritDoc}
     */
    public void executeCommand(final ProgressId aProgressId, long aApplicationId, String aCommand, Map<String, String> aEnv) {

        Application application = theApplicationDao.getApplicationById(aApplicationId);
        Host host = theHostService.getHostById(application.getHostId());

        String key = aApplicationId + " - " + aCommand;
        Integer previousCount = thePreviosCommandLinesCount.get(key);
        final int max = 2 + (previousCount!=null ? previousCount : 0) ;

        theProgressManagerService.startProgress(aProgressId
                , String.format("Run %s / %s / %s ...", host.getHostname(), application.getApplicationName(), aCommand)
                , max
        );

        User user = theUserDao.getUserById(application.getUserId());

        try {

            CommandInfo command = theCommandInfoService.getCommandInfo(aApplicationId, aCommand);

            // creates tar-gz archive
            String uniqueId = "ch-"+System.currentTimeMillis();
            File tarGzFile = createTarGzArchive(uniqueId, command.getLocalFile(), aEnv);

            HttpProxyInfo httpProxyInfo = createProxyInfo(host.getHttpProxy());
            // copy command to remote host
            theProgressManagerService.setProgressText(aProgressId, String.format("Copy file %s to %s...", aCommand, host.getHostname()));
            theSecurityShellService.copyFileToRemoteHost(host.getHostname()
                    , host.getSshPort()
                    , user.getUsername()
                    , user.getPassword()
                    , tarGzFile
                    , "."
                    , httpProxyInfo
            );
            theProgressManagerService.incrementProgressValue(aProgressId);

            // executes command
            theProgressManagerService.setProgressText(aProgressId, String.format("Executing command %s ...", aCommand));

            final AtomicInteger count = new AtomicInteger(0);
            theSecurityShellService.executeCommand(host.getHostname()
                    , host.getSshPort()
                    , user.getUsername()
                    , user.getPassword()
                    , aEnv
//                    , String.format("chmod +x ./%s && ./%s && rm ./%s", aCommand, aCommand, aCommand)
                    , String.format("tar xzf %s && cd %s && chmod +x *.sh && source setenv.sh && ./%s", tarGzFile.getName(), uniqueId, aCommand)
                    , null
                    , new ICommandOutputListener() {
                public void onOutputLine(Level aLevel, String aLine) {
                    // increment lines count
                    int currentCount = count.incrementAndGet();
                    if(currentCount + 2 <= max) {
                        theProgressManagerService.incrementProgressValue(aProgressId);
                    }
                    
                    if(aLevel == Level.ERROR) {
                        theProgressManagerService.error(aProgressId, aLine);
                    } else {
                        theProgressManagerService.info(aProgressId, aLine);
                    }
                }
            }
                    , httpProxyInfo
            );
            theProgressManagerService.incrementProgressValue(aProgressId);

            theProgressManagerService.finishProgress(aProgressId);
            thePreviosCommandLinesCount.put(key, count.get());
        } catch (Exception e) {
            theProgressManagerService.progressFailed(aProgressId, e);
        }

    }

    /**
     * Creates tar gz archive
     * @param aUniqueId     unique id
     * @param aCommandFile  command file to include in archive
     * @param aEnv          environment variables
     * @return              tar gz archive file
     * @throws IOException on io error
     * @throws InterruptedException on creating tar archive error
     */
    protected File createTarGzArchive(String aUniqueId, File aCommandFile, Map<String, String> aEnv) throws IOException, InterruptedException {

        // make
        File dir = new File(theArchivesDirectory, aUniqueId);
        dir.mkdirs();

        // creates env file
        PrintWriter out = new PrintWriter(new File(dir, "setenv.sh"));
        try {
            for (Map.Entry<String, String> entry : aEnv.entrySet()) {
                out.printf("export %s=%s\n",entry.getKey(), entry.getValue()) ;
            }
        } finally {
            out.close();
        }

        // copy command file
        copyFile(aCommandFile, new File(dir, aCommandFile.getName()));

        // executes tar czf
        File file = new File(theArchivesDirectory, aUniqueId+".tgz");
        String cmd = String.format("tar czf %s %s", file.getName(), aUniqueId);
        Process process = Runtime.getRuntime().exec(cmd, null, new File(theArchivesDirectory));
        int result = process.waitFor();
        Assert.isTrue(result==0, "Could not execute "+cmd);
        return file;
    }

    private void copyFile(File aSource, File aDest) throws IOException {
        final int SIZE = 2048;
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(aDest), SIZE);
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(aSource), SIZE);
            try {
                byte[] buf = new byte[SIZE];
                int count;
                while ( (count = in.read(buf, 0, SIZE)) >= 0) {
                    out.write(buf, 0, count);
                }
            } finally {
                in.close();
            }
        } finally {
            out.close();
        }
    }

    /**
     * Creates proxy info if proxy is setted to host
     * @param aProxy proxy
     * @return proxy info
     */
    private HttpProxyInfo createProxyInfo(HttpProxy aProxy) {
        return aProxy!=null ? new HttpProxyInfo(aProxy.getHostname(), aProxy.getPort()) : null;
    }

    /**
     * ISecurityShellService
     *
     * @param aSecurityShellService ssh service
     */
    public void setSecurityShellService(ISecurityShellService aSecurityShellService) {
        theSecurityShellService = aSecurityShellService;
    }

    /**
     * IProgressManagerService
     *
     * @param aProgressManagerService progress service
     */
    public void setProgressManagerService(IProgressManagerService aProgressManagerService) {
        theProgressManagerService = aProgressManagerService;
    }

    /**
     * IHostService
     *
     * @param aHostService host service
     */
    public void setHostService(IHostService aHostService) {
        theHostService = aHostService;
    }

    /**
     * Application dao
     *
     * @param aApplicationDao app dao
     */
    public void setApplicationDao(IApplicationDao aApplicationDao) {
        theApplicationDao = aApplicationDao;
    }

    /**
     * User dao
     *
     * @param aUserDao user dao
     */
    public void setUserDao(IUserDao aUserDao) {
        theUserDao = aUserDao;
    }

    /**
     * ICommandInfoService
     *
     * @param aCommandInfoService command info service
     */
    public void setCommandInfoService(ICommandInfoService aCommandInfoService) {
        theCommandInfoService = aCommandInfoService;
    }

    /** Archives directory */
    public void setArchivesDirectory(String aArchivesDirectory) { theArchivesDirectory = aArchivesDirectory ; }

    /** Archives directory */
    private String theArchivesDirectory = "archives";
    /**
     * ICommandInfoService
     */
    private ICommandInfoService theCommandInfoService;
    /**
     * User dao
     */
    private IUserDao theUserDao;
    /**
     * Application dao
     */
    private IApplicationDao theApplicationDao;
    /**
     * IHostService
     */
    private IHostService theHostService;
    /**
     * IProgressManagerService
     */
    private IProgressManagerService theProgressManagerService;
    /**
     * ISecurityShellService
     */
    private ISecurityShellService theSecurityShellService;

    private Map<String, Integer> thePreviosCommandLinesCount = Collections.synchronizedMap(new HashMap<String, Integer>());
}
