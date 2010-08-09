package com.googlecode.charpa.web.progress;

import com.googlecode.charpa.progress.service.IProgressInfoService;
import com.googlecode.charpa.progress.service.IProgressInfo;
import com.googlecode.charpa.web.util.FormatUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.PageParameters;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;

/**
 * List progresses
 */
public class ListProgressesPanel extends Panel {

    /**
     * {@inheritDoc}
     */
    public ListProgressesPanel(String aId, final Class<? extends Page> aProgressPanelClass, IProgressInfoService aProgressInfoService) {
        super(aId);

        theProgressInfoService = aProgressInfoService;
        
        WebMarkupContainer panel = new WebMarkupContainer("progresses-panel");
        add(panel);

        AbstractReadOnlyModel model = new AbstractReadOnlyModel() {
            public Object getObject() {
                return theProgressInfoService.listProgresses();
            }
        };
        panel.add(new ListView<IProgressInfo>("progresses", model) {
            protected void populateItem(ListItem<IProgressInfo> aItem) {
                final IProgressInfo info = aItem.getModelObject();
                aItem.add(new Label("progress-value", String.valueOf(info.getCurrentValue())));
                aItem.add(new Label("progress-max", String.valueOf(info.getMax())));
                aItem.add(new Label("progress-state", String.valueOf(info.getState())));
                aItem.add(new Label("progress-text", info.getProgressText()));
                aItem.add(new Label("progress-started", FormatUtils.formatDateTime(info.getStartedTime())));

                aItem.add(new Label("progress-running-time", FormatUtils.formatPeriod(info.getElapsedPeriod())));

                PageParameters params = new PageParameters();
                params.put("id", info.getId().toString());
                BookmarkablePageLink link = new BookmarkablePageLink("progress-link", aProgressPanelClass, params);
                aItem.add(link);
                link.add(new Label("progress-name", info.getName()));

                aItem.add(new AttributeModifier("class", true
                        , new Model(
                        (aItem.getIndex()%2 == 0 ? "odd" : "even") +
                        (" state-"+info.getState().toString()).toLowerCase()))
                );
            }
        });

        panel.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(3)));
    }

    private final IProgressInfoService theProgressInfoService;
}
