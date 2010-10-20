package com.googlecode.charpa.web.page.progress;

import com.googlecode.charpa.progress.service.IProgressInfoService;
import com.googlecode.charpa.web.page.BasePage;
import com.googlecode.charpa.web.progress.ProgressPanel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Progress page
 */
public class ProgressPage extends BasePage {

    public ProgressPage(PageParameters aParameters) {
        add(new ProgressPanel("progress-panel", aParameters, theProgressInfoService));
    }

    @SpringBean
    IProgressInfoService theProgressInfoService;
}
