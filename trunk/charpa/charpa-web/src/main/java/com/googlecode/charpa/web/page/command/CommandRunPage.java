package com.googlecode.charpa.web.page.command;

import com.googlecode.charpa.web.page.BasePage;
import com.googlecode.charpa.web.page.progress.ProgressPage;
import com.googlecode.charpa.service.ICommandInfoService;
import com.googlecode.charpa.service.ICommandService;
import com.googlecode.charpa.service.model.VariableInfo;
import com.googlecode.charpa.service.domain.CommandInfo;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.progress.service.IProgressInfoService;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.IOException;

/**
 * Page to put parameters to command
 */
public class CommandRunPage extends BasePage {

    public CommandRunPage(PageParameters aParameters) throws IOException {
        long appId = aParameters.getAsLong("appId");
        String commandName = aParameters.getString("command");
        CommandInfo commandInfo = theCommandInfoService.getCommandInfo(appId, commandName);

        add(new Label("command-name", commandName));
        add(new ListView<VariableInfo>("variables", commandInfo.getVariables()) {
            protected void populateItem(ListItem<VariableInfo> aItem) {
                VariableInfo info = aItem.getModelObject();
                aItem.add(new Label("variable-name", info.getName()));
                aItem.add(new Label("variable-default-value", info.getDefaultValue()));
                aItem.add(new Label("variable-comment", info.getComment()));
            }
        });

        add(new RunCommandLink("run", commandInfo, appId, commandName));
    }

    private class RunCommandLink extends Link {
        public RunCommandLink(String id, CommandInfo aCommand, long aApplicationId, String aCommandName) {
            super(id);
            theCommand = aCommand;
            theCommandName = aCommandName;
            theApplicationId = aApplicationId;
        }

        public void onClick() {
            final ProgressId progressId = theProgressInfoService.createProgressId(
                    String.format("Run %s/%s/%s"
                            , theCommand.getHostname(), theCommand.getApplicationName(), theCommandName));
            theProgressInfoService.invoke(progressId, new Runnable() {
                public void run() {
                    theCommandService.executeCommand(progressId, theApplicationId, theCommandName);
                }
            });
            PageParameters parameters = new PageParameters();
            parameters.put("id", progressId.toString());
            setResponsePage(ProgressPage.class, parameters);
        }

        private final CommandInfo theCommand;
        private final String theCommandName;
        private final long theApplicationId;

        @SpringBean
        private IProgressInfoService theProgressInfoService;
        @SpringBean
        private ICommandService theCommandService;
    }


    @SpringBean
    private ICommandInfoService theCommandInfoService;
}
