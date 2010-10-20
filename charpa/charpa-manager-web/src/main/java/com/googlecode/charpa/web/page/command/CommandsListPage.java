package com.googlecode.charpa.web.page.command;

import com.googlecode.charpa.web.page.BasePage;
import com.googlecode.charpa.web.page.progress.ProgressPage;
import com.googlecode.charpa.service.ICommandInfoService;
import com.googlecode.charpa.service.ICommandService;
import com.googlecode.charpa.service.model.CommandForList;
import com.googlecode.charpa.progress.service.IProgressInfoService;
import com.googlecode.charpa.progress.service.ProgressId;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.PageParameters;

/**
 * List of commands
 */
public class CommandsListPage extends BasePage {

    public CommandsListPage() {
        add(new ListView<CommandForList>("commands", theCommandInfoService.getAllCommands()) {
            protected void populateItem(ListItem<CommandForList> aItem) {
                CommandForList cmd = aItem.getModelObject();
                aItem.add(new Label("command-application", cmd.getApplicationName()));
                aItem.add(new Label("command-host", cmd.getHostname()));

                PageParameters parameters = new PageParameters();
                parameters.put("appId", String.valueOf(cmd.getApplicationId()));
                parameters.put("command", cmd.getCommandName());
                aItem.add(
                        new BookmarkablePageLink<CommandRunPage>("command-run-link", CommandRunPage.class, parameters)
                        .add(new Label("command-name", cmd.getCommandName()))                        
                );
//                aItem.add(
//                        new RunCommandLink("command-run-link", cmd)
//                        .add(new Label("command-name", cmd.getCommandName()))
//                );
            }
        });
    }

    @SpringBean
    private ICommandInfoService theCommandInfoService;

}
