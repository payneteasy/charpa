package com.googlecode.charpa.web.page.command;

import com.googlecode.charpa.progress.service.IProgressInfoService;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.service.ICommandInfoService;
import com.googlecode.charpa.service.ICommandService;
import com.googlecode.charpa.service.domain.CommandInfo;
import com.googlecode.charpa.service.model.VariableInfo;
import com.googlecode.charpa.web.page.BasePage;
import com.googlecode.charpa.web.page.progress.ProgressPage;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;

/**
 * Page to put parameters to command
 */
public class CommandRunPage extends BasePage {

    public CommandRunPage(PageParameters aParameters) throws IOException {
        long appId = aParameters.getAsLong("appId");
        String commandName = aParameters.getString("command");
        CommandInfo commandInfo = theCommandInfoService.getCommandInfo(appId, commandName);
        theVariables = commandInfo.getVariables();

        add(new Label("command-name", commandName));

        Form form = new Form("form");
        add(form);
        
        form.add(new ListView<VariableInfo>("variables", theVariables) {
            protected void populateItem(ListItem<VariableInfo> aItem) {
                VariableInfo info = aItem.getModelObject();
                aItem.add(new Label("variable-name", info.getName()));
                aItem.add(new Label("variable-default-value", info.getDefaultValue()));
                aItem.add(new Label("variable-comment", info.getComment()));
                aItem.add(new TextField<String>("variable-value", new PropertyModel<String>(info, "value")));
            }
        });

        form.add(new RunButton("run", commandInfo, appId, commandName));
    }

    private class RunButton extends Button {
        public RunButton(String id, CommandInfo aCommand, long aApplicationId, String aCommandName) {
            super(id);
            theCommand = aCommand;
            theCommandName = aCommandName;
            theApplicationId = aApplicationId;
        }

        public void onSubmit() {
            final ProgressId progressId = theProgressInfoService.createProgressId(
                    String.format("Run %s/%s/%s"
                            , theCommand.getHostname(), theCommand.getApplicationName(), theCommandName));

            theProgressInfoService.invoke(progressId, new Runnable() {
                public void run() {
                    // creates env variables
                    HashMap<String, String> env = new HashMap<String, String>();
                    for (VariableInfo info : theVariables) {
                        if(StringUtils.hasText(info.getValue())) {
                            env.put(info.getName(), info.getValue());
                        }
                    }
                    // run command
                    theCommandService.executeCommand(progressId, theApplicationId, theCommandName, env);
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

    private final List<VariableInfo> theVariables;
    @SpringBean
    private ICommandInfoService theCommandInfoService;
}
