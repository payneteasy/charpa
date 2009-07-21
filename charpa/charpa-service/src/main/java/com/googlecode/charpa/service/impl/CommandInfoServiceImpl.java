package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.service.ICommandInfoService;
import com.googlecode.charpa.service.dao.IApplicationDao;
import com.googlecode.charpa.service.dao.IHostDao;
import com.googlecode.charpa.service.model.CommandForList;
import com.googlecode.charpa.service.model.VariableInfo;
import com.googlecode.charpa.service.domain.CommandInfo;
import com.googlecode.charpa.service.domain.Application;
import com.googlecode.charpa.service.domain.Host;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of ICommandInfoService
 */
public class CommandInfoServiceImpl implements ICommandInfoService {

    private final Logger LOG = LoggerFactory.getLogger(CommandInfoServiceImpl.class);

    // $VARIABLE=${ENV_VARIABLE:-default}
    public static final Pattern VARIABLE_PATTERN = Pattern.compile("^(.*?)=\\$\\{(.*)\\:-(.*)\\}");
    // # comment
    public static final Pattern COMMENT_PATTERN  = Pattern.compile("^#(.*)");

    /**
     * {@inheritDoc}
     */
    public CommandInfo getCommandInfo(long aApplicationId, String aCommandName) throws IOException {
        CommandInfo info = new CommandInfo();

        File appDir = new File(theCommandsDir, String.valueOf(aApplicationId));
        if(!appDir.exists()) throw new IllegalStateException("Directory "+appDir.getAbsolutePath()+" is not exists");

        File commandFile = new File(appDir, aCommandName);
        if(!commandFile.exists()) throw new IllegalStateException("Command "+commandFile.getAbsolutePath()+" is not exists");
        info.setLocalFile(commandFile);

        Application application = theApplicationDao.getApplicationById(aApplicationId);
        info.setApplicationName(application.getApplicationName());

        Host host = theHostDao.getHostById(aApplicationId);
        info.setHostname(host.getHostname());
        
        // adds variables infos
        addVariablesInfos(info);
        return info;
    }

    private void addVariablesInfos(CommandInfo aCommandInfo) throws IOException {
        LineNumberReader in = new LineNumberReader(new FileReader(aCommandInfo.getLocalFile()));
        try {
            String line;
            String comment = null;
            while ( (line=in.readLine()) !=null) {
                // VARIABLE
                if(VARIABLE_PATTERN.matcher(line).find()) {
                    VariableInfo variable = createVariableInfo(line, comment);
                    if(variable!=null) {
                        aCommandInfo.getVariables().add(variable);
                        // clear comment
                        comment = null;
                    } else {
                        LOG.warn("Cannot create variable info for line "+line);
                    }
                } else {
                    // COMMENT
                    Matcher commentMatcher = COMMENT_PATTERN.matcher(line);
                    if(commentMatcher.find()) {
                        comment = commentMatcher.group(1).trim();
                    }
                }
            }
        } finally {
            in.close();
        }
    }

    private VariableInfo createVariableInfo(String aLine, String aComment) {
        Matcher matcher = VARIABLE_PATTERN.matcher(aLine);
        if(matcher.find() && matcher.groupCount()==3) {
            VariableInfo info = new VariableInfo();
            info.setComment(aComment);
            info.setName(matcher.group(2));
            info.setDefaultValue(matcher.group(3));
            return info;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<CommandForList> getAllCommands() {
        List<CommandForList> list = new LinkedList<CommandForList>();
        List<Application> applications = theApplicationDao.getAllApplications();
        for (Application application : applications) {
            File appDir = new File(theCommandsDir, String.valueOf(application.getId()));
            if(appDir.exists() && appDir.isDirectory()) {
                for (File file : appDir.listFiles()) {
                    if(file.isFile() && file.exists()) {
                        CommandForList cmd = new CommandForList();
                        cmd.setCommandName(file.getName());
                        cmd.setApplicationId(application.getId());
                        cmd.setApplicationName(application.getApplicationName());
                        cmd.setHostname(theHostDao.getHostById(application.getHostId()).getName());
                        list.add(cmd);
                    }
                }
            }
        }
        return list;
    }

    /**
     * Command dir name
     * @param aCommandsDirName commands dir name
     */
    public void setCommandsDirName(String aCommandsDirName) {

        theCommandsDir = new File(aCommandsDirName);
    }

    /** IApplicationDao */
    public void setApplicationDao(IApplicationDao aApplicationDao) { theApplicationDao = aApplicationDao ; }

    /** IHostDao */
    public void setHostDao(IHostDao aHostDao) { theHostDao = aHostDao ; }

    /** IHostDao */
    private IHostDao theHostDao ;
    /** IApplicationDao */
    private IApplicationDao theApplicationDao ;
    /**
     * Command dir name
     */
    private File theCommandsDir;
}
