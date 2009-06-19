package com.googlecode.charpa.web.progress;

import com.googlecode.charpa.progress.service.*;
import com.googlecode.charpa.web.component.ConfirmAjaxLink;
import com.googlecode.charpa.web.util.FormatUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.*;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Displays progress info
 */
public class ProgressPanel extends Panel {

    public ProgressPanel(String aId, final PageParameters aParameters) {
        super(aId);
        String idString = aParameters.getString("id");
        Assert.hasText(idString, "There was no id paramater given");

        WebMarkupContainer panel = new WebMarkupContainer("panel");
        add(panel);

        final ProgressId id = new ProgressId(idString);

        final AbstractReadOnlyModel<IProgressInfo> model = new AbstractReadOnlyModel<IProgressInfo>() {
            public IProgressInfo getObject() {
                return theProgressInfoService.getProgressInfo(id);
            }
        };

        setDefaultModel(model);
        panel.add(new Label("progress-name", new CompoundPropertyModel(model).bind("name")));
        panel.add(new Label("progress-text", new CompoundPropertyModel(model).bind("progressText")));
        panel.add(new Label("progress-max", new CompoundPropertyModel(model).bind("max")));
        panel.add(new Label("progress-value", new CompoundPropertyModel(model).bind("currentValue")));
        panel.add(new Label("progress-state", new CompoundPropertyModel(model).bind("state")));

        WebMarkupContainer progressDone = new WebMarkupContainer("progress-done");
        panel.add(progressDone);
        progressDone.add(new AttributeModifier("width", true, new AbstractReadOnlyModel() {
            public Object getObject() {
                IProgressInfo info = model.getObject();
                return Math.round((info.getCurrentValue() / (float)info.getMax()) * 400);
            }
        }));

        WebMarkupContainer progressToBeDone = new WebMarkupContainer("progress-tobedone");
        panel.add(progressToBeDone);
        progressToBeDone.add(new AttributeModifier("width", true, new AbstractReadOnlyModel() {
            public Object getObject() {
                IProgressInfo info = (IProgressInfo) model.getObject();
                return 400 - Math.round((info.getCurrentValue() / (float)info.getMax()) * 400);            
            }
        }));

        panel.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(3)) {
            @Override
            protected void onPostProcessTarget(AjaxRequestTarget aTarget) {
                IProgressInfo info = (IProgressInfo) model.getObject();
                PageParameters pageParameters = info.getPageParameters().isEmpty()
                                    ? aParameters : new PageParameters(info.getPageParameters());
                if(pageParameters.get(ProgressParameters.NEXT_PAGE)!=null) {
                    try {
                        Class pageClass = Class.forName(pageParameters.getString(ProgressParameters.NEXT_PAGE));
                        if(info.getState() == ProgressState.FINISHED) {
                            setResponsePage(pageClass, pageParameters);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
        });

        panel.add(new ConfirmAjaxLink("cancel-link", "Are you sure to cancel project run?") {

            public void onClick(AjaxRequestTarget target) {
                theProgressInfoService.cancelProgress(id);
            }

            public boolean isVisible() {
                IProgressInfo info = model.getObject();
                return info.getState() == ProgressState.PENDING || info.getState() == ProgressState.RUNNING;
            }
        });

        // time
        panel.add(new Label("created-time", new AbstractReadOnlyModel() {
            public Object getObject() {
                return FormatUtils.formatDateTime(model.getObject().getCreatedTime());
            }
        }) {
            public boolean isVisible() {
                return model.getObject().getCreatedTime()!=null;
            }
        });

        panel.add(new Label("started-time", new AbstractReadOnlyModel() {
            public Object getObject() {
                return FormatUtils.formatDateTime(model.getObject().getStartedTime());
            }
        }) {
            public boolean isVisible() {
                return model.getObject().getStartedTime()!=null;
            }
        });

        panel.add(new Label("ended-time", new AbstractReadOnlyModel() {
            public Object getObject() {
                return FormatUtils.formatDateTime(model.getObject().getEndedTime());
            }
        }) {
            public boolean isVisible() {
                return model.getObject().getEndedTime()!=null;
            }
        });

        panel.add(new Label("time-elapsed", new AbstractReadOnlyModel() {
            public Object getObject() {
                return FormatUtils.formatPeriod(model.getObject().getElapsedPeriod());
            }
        }) {
            public boolean isVisible() {
                return model.getObject().getElapsedPeriod()!=null;
            }
        });

        panel.add(new Label("time-left", new AbstractReadOnlyModel() {
            public Object getObject() {
                return FormatUtils.formatPeriod(model.getObject().getLeftPeriod());
            }
        }) {
            public boolean isVisible() {
                return model.getObject().getLeftPeriod()!=null;
            }
        });

        // log messages
        LoadableDetachableModel<List<LogMessage>> logMessagesModel = new LoadableDetachableModel<List<LogMessage>>() {
            protected List<LogMessage> load() {
                return model.getObject().getLastLogMessages(20);
            }
        };
        panel.add(new ListView<LogMessage>("log-messages", logMessagesModel) {
            protected void populateItem(ListItem<LogMessage> aItem) {
                aItem.add(new Label("log-message", aItem.getModelObject().getMessage()));
            }
        });
    }

    @SuppressWarnings({"UnusedDeclaration"})
    @SpringBean
    private IProgressInfoService theProgressInfoService;
}
